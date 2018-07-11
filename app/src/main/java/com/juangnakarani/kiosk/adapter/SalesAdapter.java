package com.juangnakarani.kiosk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangnakarani.kiosk.R;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHoler> {
    private String[] mDataset;

    public SalesAdapter(String[] myDataset) {
        mDataset = myDataset;
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
        holder.title.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class SalesViewHoler extends RecyclerView.ViewHolder{
        public TextView title, year, genre;
        public SalesViewHoler(View v) {
            super(v);
//            mTextView = itemView;
        }
    }
}
