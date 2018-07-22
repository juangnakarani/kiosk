package com.juangnakarani.kiosk;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.juangnakarani.kiosk.adapter.DeviceAdapter;
import com.juangnakarani.kiosk.model.Device;
import com.juangnakarani.kiosk.model.Sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    protected static final String TAG = "TAG";
    private BluetoothAdapter mBluetoothAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mPairedDevicesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Device> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rclv_devices);
        mRecyclerView.setHasFixedSize(true);

        mPairedDevicesAdapter = new DeviceAdapter(devices);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPairedDevicesAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice s : mPairedDevices){
            Log.i("chk device",s.getName());
            Device d = new Device(s.getAddress(),s.getName());
            devices.add(d);
        }

        mPairedDevicesAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) {

            try {


                mBluetoothAdapter.cancelDiscovery();
                String mDeviceInfo = ((TextView) mView).getText().toString();
                String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
                Log.v(TAG, "Device_Address " + mDeviceAddress);

                Bundle mBundle = new Bundle();
                mBundle.putString("DeviceAddress", mDeviceAddress);
                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(mBundle);
                setResult(Activity.RESULT_OK, mBackIntent);
                finish();
            } catch (Exception ex) {

            }
        }
    };
}
