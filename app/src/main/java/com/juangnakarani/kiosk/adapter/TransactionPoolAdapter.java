package com.juangnakarani.kiosk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.model.TransactionHeader;

import java.util.List;

public class TransactionPoolAdapter extends RecyclerView.Adapter<TransactionPoolAdapter.TransactionPoolViewHoler> {
    private List<TransactionHeader> transactionHeaderList;

    public TransactionPoolAdapter(List<TransactionHeader> transactionHeaderList) {
        this.transactionHeaderList = transactionHeaderList;
    }

    @NonNull
    @Override
    public TransactionPoolViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_transaction_pool, parent, false);

        return new TransactionPoolViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionPoolViewHoler holder, int position) {
        TransactionHeader th = transactionHeaderList.get(position);
        holder.id.setText(String.valueOf(th.getId()));
        holder.datetime.setText(th.getDatetime());
        holder.total.setText(String.valueOf(th.getTotal()));
    }

    @Override
    public int getItemCount() {
        return transactionHeaderList.size();
    }

    public class TransactionPoolViewHoler extends RecyclerView.ViewHolder {
        private TextView datetime, total, id;
        public TransactionPoolViewHoler(View itemView) {
            super(itemView);
            this.datetime = itemView.findViewById(R.id.transaction_datetime);
            this.total = itemView.findViewById(R.id.transaction_total);
            this.id = itemView.findViewById(R.id.transaction_id);
        }
    }
}
