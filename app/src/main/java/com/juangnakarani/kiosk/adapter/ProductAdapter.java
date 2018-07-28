package com.juangnakarani.kiosk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHoler> {
    private List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_product, parent, false);
        return new ProductViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHoler holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHoler extends RecyclerView.ViewHolder {
        public TextView name, price;

        public ProductViewHoler(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.product_name);
            this.price = itemView.findViewById(R.id.product_price);
        }
    }
}
