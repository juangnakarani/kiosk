package com.juangnakarani.kiosk.adapter;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHoler> {
    private List<Product> products;
    private DbHelper db;

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
        holder.id = product.getId();
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice().toString());
        holder.ordered.setText(String.valueOf(product.getOrdered()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int id;
        private TextView name, price, ordered;
        protected ImageButton btnDecrementOrder;

        public ProductViewHoler(View itemView) {
            super(itemView);


            this.name = itemView.findViewById(R.id.product_name);
            this.price = itemView.findViewById(R.id.product_price);
            this.ordered = itemView.findViewById(R.id.product_ordered);
            this.btnDecrementOrder = itemView.findViewById(R.id.product_decreament);

            itemView.setOnClickListener(this);
            btnDecrementOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            db = new DbHelper(view.getContext());
            if(view.getId() == btnDecrementOrder.getId()){
                Log.i("chk","decrement order " + getLayoutPosition() + " -- " + id + name.getText().toString() + price.getText().toString() );
                int i = db.decrementOrder(id);
            }else{
                Log.i("chk","onClick " + getLayoutPosition() + " " + name.getText().toString() + price.getText().toString() );
                int i = db.incrementOrder(id);
                Log.i("chk", "return update " + i);
            }
            Product p = db.getProductByID(id);
            Log.i("chk price ", p.getPrice().toString());
            products.set(getLayoutPosition(), p);

            notifyItemChanged(getLayoutPosition());
        }
    }


}
