package com.juangnakarani.kiosk.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.model.Device;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHoler> {
    private List<Device> devices;

    public DeviceAdapter(List<Device> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_device, parent, false);
        return new DeviceViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHoler holder, int position) {
        Device device = devices.get(position);
        holder.name.setText(device.getName());
        holder.address.setText(device.getAddress());
        if(device.isSelected()==true){
            holder.icon.setImageResource(R.drawable.ic_printshop);
            holder.icon.setColorFilter(Color.argb(255, 0, 0, 0));
        }else{
            holder.icon.setImageResource(R.drawable.ic_outline_print_disabled);
            holder.icon.setColorFilter(Color.argb(255, 200, 200, 200));
        }

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class DeviceViewHoler extends RecyclerView.ViewHolder {
        public TextView address, name;
        public ImageButton icon;

        public DeviceViewHoler(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.device_name);
            this.address = (TextView) v.findViewById(R.id.device_address);
            this.icon = v.findViewById(R.id.device_icon);

        }
    }
}
