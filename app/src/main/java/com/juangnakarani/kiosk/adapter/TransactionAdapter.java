package com.juangnakarani.kiosk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Product;
import com.juangnakarani.kiosk.other.RupiahFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.juangnakarani.kiosk.other.RupiahFormat.formatRupiah;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHoler> {
    private List<Product> products;
//    private DbHelper db;

    public TransactionAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public TransactionViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_transaction, parent, false);

        return new TransactionAdapter.TransactionViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHoler holder, int position) {

        Product product = products.get(position);
        holder.id = product.getId();
        holder.name.setText(product.getName());
        String priceMultiplyOrder = product.getOrdered() + " x " + product.getPrice().toString();

        holder.price.setText(priceMultiplyOrder);
//        holder.ordered.setText(String.valueOf(product.getOrdered()));
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(product.getOrdered()));
        holder.total.setText(formatRupiah(total));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class TransactionViewHoler extends RecyclerView.ViewHolder{
        private int id;
        private TextView name, price, total;

        public TransactionViewHoler(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.product_name);
            this.price = itemView.findViewById(R.id.product_price);
//            this.ordered = itemView.findViewById(R.id.product_ordered);
            this.total = itemView.findViewById(R.id.product_total);
        }
    }
}
