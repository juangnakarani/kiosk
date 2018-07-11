package com.juangnakarani.kiosk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.model.Sales;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHoler> {
//    private String[] mDataset;
    private List<Sales> listSales;

    public SalesAdapter(List<Sales> sales) {
        this.listSales = sales;
    }

    @NonNull
    @Override
    public SalesViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_list_row, parent, false);

        return new SalesViewHoler(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull SalesViewHoler holder, int position) {
// - get element from your dataset at this position
        // - replace the contents of the view with that element
        Sales sales = listSales.get(position);
        holder.id.setText(String.valueOf(sales.getID()));
        holder.total.setText(String.valueOf(sales.getTotal()));
        holder.date.setText(String.valueOf(sales.getDate()));
    }

    @Override
    public int getItemCount() {
        Log.i("chk", "listSales.size");
        Log.i("chk", String.valueOf(listSales.size()));
        return listSales.size();
    }

    public static class SalesViewHoler extends RecyclerView.ViewHolder{
        public TextView id, total, date;
        public SalesViewHoler(View v) {
            super(v);
            id = (TextView) v.findViewById(R.id.id);
            total = (TextView) v.findViewById(R.id.total);
            date = (TextView) v.findViewById(R.id.date);
        }
    }
}
