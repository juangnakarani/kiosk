package com.juangnakarani.kiosk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.juangnakarani.kiosk.adapter.DeviceAdapter;
import com.juangnakarani.kiosk.model.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrinterActivity extends AppCompatActivity implements IClickListener{
    private BluetoothAdapter mBluetoothAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mPairedDevicesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Device> devices = new ArrayList<>();
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        mRecyclerView = (RecyclerView) findViewById(R.id.rclv_devices);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new IClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("chk", "onClick mRecyclerView");
                Toast.makeText(PrinterActivity.this, "Long click for select this device!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.d("chk", "onLongClick mRecyclerView");
                for(Device d: devices){
                    d.setSelected(false);
                }
                Device d = devices.get(position);
                d.setSelected(true);
                mPairedDevicesAdapter.notifyDataSetChanged();
//                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.pref_key_printer), d.getAddress());
                editor.commit();

                String defaultValue = getResources().getString(R.string.pref_key_printer);
                String printerValue = sharedPref.getString(getString(R.string.pref_key_printer), defaultValue);

                Toast.makeText(PrinterActivity.this, "Selected printer: " +d.getName()+ " (" + printerValue + ")", Toast.LENGTH_SHORT).show();
            }
        }));

        mPairedDevicesAdapter = new DeviceAdapter(devices);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPairedDevicesAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();

        String defaultValue = getResources().getString(R.string.pref_key_printer);
        String printerValue = sharedPref.getString(getString(R.string.pref_key_printer), defaultValue);

        for (BluetoothDevice s : mPairedDevices){
//            Log.i("chk device",s.getName());
            Device d = new Device(s.getAddress(),s.getName(),false);
            if(s.getAddress().equals(printerValue)){
                d.setSelected(true);
            }
            devices.add(d);
        }

        mPairedDevicesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private IClickListener clicklistener;
        private GestureDetector gestureDetector;

        RecyclerTouchListener(Context context, final RecyclerView recycleView, final IClickListener clicklistener) {
            this.clicklistener = clicklistener;
            this.gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
