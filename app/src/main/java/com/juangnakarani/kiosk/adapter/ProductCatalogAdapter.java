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

import java.util.List;

public class ProductCatalogAdapter extends RecyclerView.Adapter<ProductCatalogAdapter.ProductCatalogViewHoler> {
    private List<Product> products;
    private DbHelper db;

    public ProductCatalogAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductCatalogViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_product_catalog, parent, false);

        return new ProductCatalogAdapter.ProductCatalogViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCatalogViewHoler holder, int position) {
        Product product = products.get(position);
        holder.id.setText(String.valueOf(product.getId()));
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductCatalogViewHoler extends RecyclerView.ViewHolder {
        private TextView id, name, price;

        public ProductCatalogViewHoler(View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.product_id);
            this.name = itemView.findViewById(R.id.product_name);
            this.price = itemView.findViewById(R.id.product_price);
        }
    }
}
