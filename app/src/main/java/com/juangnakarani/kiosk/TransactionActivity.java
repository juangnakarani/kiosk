package com.juangnakarani.kiosk;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.juangnakarani.kiosk.adapter.TransactionAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Product;
import com.juangnakarani.kiosk.model.TransactionDetail;
import com.juangnakarani.kiosk.model.TransactionHeader;
import com.juangnakarani.kiosk.model.User;
import com.juangnakarani.kiosk.other.UnicodeFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.juangnakarani.kiosk.other.RupiahFormat.formatRupiah;

public class TransactionActivity extends AppCompatActivity implements Runnable {
    private static final int TRANSACTION_OK = 1;
    private static final int TRANSACTION_CANCEL = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTrasactionAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> products = new ArrayList<>();
    private DbHelper db;
    private TextView mTotal, mReceived;
    private CheckBox mUangPas;
    private String totalText;
    private Button mPrint;
    private SharedPreferences sharedPref;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private String prefKioskName, prefKioskAddress, prefKioskPhone, prefKioskSlogan, prefKioskEmail, prefKioskPassword;
    private int total, receivedAmount, changeAmount;
    private boolean isAuthenticate;
    private int transactionEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transactionEvent = 0;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefKioskName = sharedPref.getString("pref_key_kiosk_name", "Not Found pref_key_kiosk_name");
        prefKioskAddress = sharedPref.getString("pref_key_kiosk_address", "Not Found pref_key_kiosk_address");
        prefKioskPhone = sharedPref.getString("pref_key_kiosk_phone", "Not Found pref_key_kiosk_phone");
        prefKioskSlogan = sharedPref.getString("pref_key_kiosk_slogan", "Not Found pref_key_kiosk_slogan");
        prefKioskEmail = sharedPref.getString("pref_key_email", "Not Found pref_key_email");
        prefKioskPassword = sharedPref.getString("pref_key_password", "Not Found pref_key_password");

        User user = new User("rizki.prisandi@gmail.com", "da9fdb45ec38a06c538689814f069341c95985577c5524c1fe67f991d2a8f52c");
//        Log.i("passwd" , user.getPassword());
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
//            Log.e("err", "NoSuchAlgorithmException", e);
        }
        md.update(prefKioskPassword.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

//        Log.i("passwd h" , hexString.toString());
//        Log.i("passwd u" , user.getPassword());
        if (prefKioskEmail.equals(user.getEmail()) && hexString.toString().equals(user.getPassword())) {
//            Log.i("passwd", "login success");
            isAuthenticate = true;
        } else {
//            Log.i("passwd", "login fail");
            isAuthenticate = false;
        }

        mRecyclerView = findViewById(R.id.rclv_transaction);
        mRecyclerView.setHasFixedSize(true);

        mTrasactionAdapter = new TransactionAdapter(products);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTrasactionAdapter);

        products.clear();
        db = new DbHelper(getApplicationContext());
        // Gets the data repository in write mode
        products.addAll(db.getOrdered());
        mTrasactionAdapter.notifyDataSetChanged();

//        Log.i("chk", "must calculated now.....");
        mTotal = findViewById(R.id.total_amount);
        total = db.calculateTotal();
        totalText = formatRupiah(total);
//        Log.i("chk", "total is " + total);

        mTotal.setText("Total: " + totalText);

        mReceived = findViewById(R.id.amount_received);
        mReceived.requestFocus();
        mUangPas = findViewById(R.id.check_uang_pas);
        mUangPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReceived.setText(String.valueOf(total));
            }
        });

        mReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.i("chk","receive changed");
                try {
                    Log.d("chk", "receive->" + mReceived.getText().toString().replace(",", ""));

                    receivedAmount = Integer.parseInt(mReceived.getText().toString().replace(",", ""));
                } catch (NumberFormatException ex) {
                    Log.e("chk", ex.toString());
                    receivedAmount = 0;
                }
                if (receivedAmount == total) {
//                    Log.i("chk","receive receivedAmount == total");
                    mUangPas.setChecked(true);
                } else {
                    mUangPas.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPrint = findViewById(R.id.btnPrint);

        mPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAuthenticate) {
                    Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                final String datePrint = simpleDateFormat.format(new Date());

//                try{
//                    receivedAmount = Integer.parseInt(mReceived.getText().toString());
//                }catch (NumberFormatException ex){
//                    receivedAmount = 0;
//                }

                if (receivedAmount == 0) {
                    Toast.makeText(view.getContext(), "Isikan jumlah dibayar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (receivedAmount < total) {
                    Toast.makeText(view.getContext(), "Kurang bayar!", Toast.LENGTH_SHORT).show();
                    return;
                }


                changeAmount = receivedAmount - total;

                Snackbar.make(view, "Uang kembalian: " + changeAmount, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
                //TODO move code bellow to function
                Date currentTime = Calendar.getInstance().getTime();
                TransactionHeader th = new TransactionHeader(currentTime.toString(), total, receivedAmount);
                long th_id = db.insertTransactionHeader(th);
//                    Log.i("chk","insertTransactionHeader->" + th_id);
                for (Product p : products) {
//                        Log.i("chk","products->" + p.getCategory().getId());
                    db.insertTransactionDetail(th_id, p);
                }
                List<TransactionDetail> transactionDetails = db.getTransactionDetailByID(th_id);
                db.setTransactionState(1);
                transactionEvent = 1;

//                String printerValue = sharedPref.getString("pref_key_printer", "not found device address");
                SharedPreferences sharedPref = getSharedPreferences("preference", Context.MODE_PRIVATE);
                String printerValue = sharedPref.getString("pref_key_printer", "not found device address");
                Log.d("chk device", printerValue);
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(printerValue);

                mBluetoothConnectProgressDialog = ProgressDialog.show(view.getContext(),
                        "Connecting...", mBluetoothDevice.getName() + " : "
                                + mBluetoothDevice.getAddress(), true, false);

                try {
                    Log.d("chk", "createRfcommSocketToServiceRecord");
                    mBluetoothSocket = mBluetoothDevice
                            .createRfcommSocketToServiceRecord(applicationUUID);
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothSocket.connect();
                    mHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    Log.d("chk IOException", "CouldNotConnectToSocket", e);
                    Toast.makeText(TransactionActivity.this, "Device Not Connected", Toast.LENGTH_LONG).show();
                    closeSocket(mBluetoothSocket);
                    return;
                }

                Thread t = new Thread() {
                    public void run() {
                        try {
                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            String BILL = "";

                            BILL = "\n"
                                    + prefKioskName + "    \n"
                                    + prefKioskSlogan + " \n"
                                    + prefKioskAddress + "  \n"
                                    + prefKioskPhone + "      \n"
                                    + String.format("%32s", datePrint) + "\n";
                            BILL = BILL + "--------------------------------\n";

                            BILL = BILL + String.format("%1$-4s %2$10s %3$14s", "Qty", "Price", "Total");
                            BILL = BILL + "\n";
                            BILL = BILL + "--------------------------------";
                            for (Product p : products) {
                                String item = p.getName().toString();
                                String ordered = String.valueOf(p.getOrdered());
                                String price = formatRupiah(p.getPrice());
                                BigDecimal total = p.getPrice().multiply(BigDecimal.valueOf(p.getOrdered()));
                                BILL = BILL + "\n " + String.format("%1$-10s", "" + item);
                                BILL = BILL + "\n " + String.format("%1$-4s %2$10s %3$14s", ordered, price, formatRupiah(total));
                            }
                            BILL = BILL + "\n--------------------------------";
                            BILL = BILL + "\n";
                            BILL = BILL + String.format("%1$-8s %2$22s", "Total", totalText);
                            BILL = BILL + "\n";
                            BILL = BILL + String.format("%1$-8s %2$22s", "Diterima", formatRupiah(receivedAmount));
                            BILL = BILL + "\n";
                            BILL = BILL + String.format("%1$-8s %2$22s", "Kembali", formatRupiah(changeAmount));
                            BILL = BILL + "\n";
                            BILL = BILL + "\n";
//                            BILL = BILL + "        Total Value:" + "     " + "700.00" + "\n";
//
//                            BILL = BILL + "----------------------\n";
                            BILL = BILL + "\n\n ";
                            os.write(BILL.getBytes());
                            //This is printer specific code you can comment ==== > Start

                            // Setting height
                            int gs = 29;
                            os.write(intToByteArray(gs));
                            int h = 104;
                            os.write(intToByteArray(h));
                            int n = 162;
                            os.write(intToByteArray(n));

                            // Setting Width
                            int gs_width = 29;
                            os.write(intToByteArray(gs_width));
                            int w = 119;
                            os.write(intToByteArray(w));
                            int n_width = 2;
                            os.write(intToByteArray(n_width));

                            byte[] open = {27, 112, 48, 55, 121};
                            os.write(open);

                        } catch (Exception e) {
                            Log.e("chk print", "printing Exception", e);
                        }
                    }
                };
                t.start();

                long delayMillis = 1000;
                try {
                    t.join(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (t.isAlive()) {
//                    Log.i("chk","thread has not finished");
                } else {
//                    Log.i("chk","thread has finished");


                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i("chk", "nganu onActivityResult TransactionActivity");
        super.onActivityResult(requestCode, resultCode, data);
        for (android.support.v4.app.Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(TransactionActivity.this, "Device Connected", Toast.LENGTH_SHORT).show();
        }
    };


    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
//            Log.i("chk", "SocketClosed");
        } catch (IOException ex) {
//            Log.i("chk", "CouldNotCloseSocket");
        }
    }

    @Override
    public void run() {
        Log.d("chk run", "run form Runnable");
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
//            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
//            Log.i("chk", "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }
}
