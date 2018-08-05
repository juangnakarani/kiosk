package com.juangnakarani.kiosk;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.juangnakarani.kiosk.adapter.TransactionAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Product;
import com.juangnakarani.kiosk.other.UnicodeFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionActivity extends AppCompatActivity implements Runnable {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTrasactionAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> products = new ArrayList<>();
    private DbHelper db;
    private TextView mTotal, mReceived;
    private CheckBox mUangPas;
    private String totalText;
    private Button mPrint;

    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private String prefKioskName, prefKioskAddress, prefKioskPhone, prefKioskSlogan;
    private int total, receivedAmount, changeAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefKioskName = prefs.getString("pref_key_kiosk_name","Not Found pref_key_kiosk_name");
        prefKioskAddress = prefs.getString("pref_key_kiosk_address","Not Found pref_key_kiosk_address");
        prefKioskPhone = prefs.getString("pref_key_kiosk_phone","Not Found pref_key_kiosk_phone");
        prefKioskSlogan = prefs.getString("pref_key_kiosk_slogan","Not Found pref_key_kiosk_slogan");

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

        Log.i("chk", "must calculated now.....");
        mTotal = findViewById(R.id.total_amount);
        total = db.calculateTotal();
        totalText = String.valueOf(total);
        Log.i("chk", "total is " + total);

        mTotal.setText("Total: " + totalText);

        mReceived = findViewById(R.id.amount_received);
        mUangPas =  findViewById(R.id.check_uang_pas);
        mUangPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReceived.setText(String.valueOf(total));
            }
        });
//        mUangPas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                mReceived.setText(String.valueOf(total));
//            }
//        });
        mReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("chk","receive changed");
                try{
                    receivedAmount = Integer.parseInt(mReceived.getText().toString());
                }catch (NumberFormatException ex){
                    receivedAmount = 0;
                }
                if(receivedAmount == total){
                    Log.i("chk","receive receivedAmount == total");
                    mUangPas.setChecked(true);
                }else{
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



                try{
                    receivedAmount = Integer.parseInt(mReceived.getText().toString());
                }catch (NumberFormatException ex){
                    receivedAmount = 0;
                }

                if(receivedAmount==0){
                    Toast.makeText(view.getContext(), "Isikan jumlah dibayar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                changeAmount = receivedAmount - total;

                Snackbar.make(view, "Uang kembalian: " + changeAmount, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice("DC:0D:30:2B:3E:04");

                mBluetoothConnectProgressDialog = ProgressDialog.show(view.getContext(),
                        "Connecting...", mBluetoothDevice.getName() + " : "
                                + mBluetoothDevice.getAddress(), true, false);

                try {
                    mBluetoothSocket = mBluetoothDevice
                            .createRfcommSocketToServiceRecord(applicationUUID);
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothSocket.connect();
                    mHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    Log.e("chk IOException", "CouldNotConnectToSocket", e);
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

                            BILL = "\n" +
                                    prefKioskName + "    \n"
                                    + prefKioskSlogan + " \n"
                                    + prefKioskAddress + "  \n"
                                    + prefKioskPhone +"      \n";
                            BILL = BILL + "--------------------------------\n";


                            BILL = BILL + String.format("%1$-4s %2$10s %3$10s", "Qty", "Price", "Total");
                            BILL = BILL + "\n";
                            BILL = BILL + "--------------------------------";
                            for (Product p : products) {
                                String item = p.getName().toString();
                                String ordered = String.valueOf(p.getOrdered());
                                String price = String.valueOf(p.getPrice());
                                BigDecimal total = p.getPrice().multiply(BigDecimal.valueOf(p.getOrdered()));
                                BILL = BILL + "\n " + String.format("%1$-10s", "" + item);
                                BILL = BILL + "\n " + String.format("%1$-4s %2$10s %3$10s", ordered, price, total.toString());
                            }
                            BILL = BILL + "\n--------------------------------";
                            BILL = BILL + "\n";
                            BILL = BILL + String.format("%1$-4s %2$10s %3$10s", "Total", "", totalText);
                            BILL = BILL + "\n";
                            BILL = BILL + String.format("%1$-7s %2$10s %3$8s", "Dibayar", "", receivedAmount);
                            BILL = BILL + "\n";
                            BILL = BILL + String.format("%1$-7s %2$10s %3$8s", "kembali", "", changeAmount);
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
                            Log.e("chk print", "printing ", e);
                        }
                    }
                };
                t.start();
            }
        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(TransactionActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
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
            Log.i("chk", "SocketClosed");
        } catch (IOException ex) {
            Log.i("chk", "CouldNotCloseSocket");
        }
    }

    @Override
    public void run() {
        Log.i("chkRun", "run form Runnable");
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
//            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.i("chk", "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }
}
