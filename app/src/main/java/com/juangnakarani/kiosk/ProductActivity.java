package com.juangnakarani.kiosk;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juangnakarani.kiosk.adapter.ProductCatalogAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Category;
import com.juangnakarani.kiosk.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements IClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mProductAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> products = new ArrayList<>();

    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rclv_product);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new IClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("chk", "onClick mRecyclerView");
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.i("chk", "onLongClick mRecyclerView");
                showProductDialog(
                        true, products.get(position), position
                );
            }
        }));

        mProductAdapter = new ProductCatalogAdapter(products);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mProductAdapter);


        db = new DbHelper(getApplicationContext());
        // Gets the data repository in write mode
        products.addAll(db.getAllProducts());

        mProductAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductDialog(false, null, -1);
            }
        });
    }

    private void showProductDialog(final boolean shouldUpdate, final Product product, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_product, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ProductActivity.this);
        alertDialogBuilderUserInput.setView(view);


        TextView dialogTitle = view.findViewById(R.id.dialog_product_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.dialog_title_product_new) : getString(R.string.dialog_title_product_edit));

        final EditText editTextProductID = view.findViewById(R.id.dialog_product_edit_id);
        final EditText editTextProductName = view.findViewById(R.id.dialog_product_edit_name);
        final EditText editTextProductPrice = view.findViewById(R.id.dialog_product_edit_price);
        final EditText editTextProductCategory = view.findViewById(R.id.dialog_product_edit_category);

        if (shouldUpdate && product != null) {
            editTextProductID.setText(String.valueOf(product.getId()));
            editTextProductName.setText(product.getName());
            editTextProductPrice.setText(String.valueOf(product.getPrice()));
            editTextProductCategory.setText(String.valueOf(product.getCategory().getId()));
        }


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(editTextProductID.getText().toString())) {
                    Toast.makeText(ProductActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // instance product
                int id = Integer.valueOf(editTextProductID.getText().toString());
                String name = editTextProductName.getText().toString();
                BigDecimal price = BigDecimal.valueOf(Integer.valueOf(editTextProductPrice.getText().toString()));
                Category category = db.getCategoryByID(Integer.valueOf(editTextProductCategory.getText().toString()));
                Product p = new Product(id, name, price, 0, category);

                // check if user updating note
                if (shouldUpdate && product != null) {
                    updateProduct(p, position);
                } else {
                    createProduct(p);
                }
            }
        });
    }

    private void createProduct(Product p) {
        long id = db.insertProduct(p);

        Product product = db.getProductByID(p.getId());
        if (product != null) {
            products.add(p);
            mProductAdapter.notifyDataSetChanged();
        }
    }

    private void updateProduct(Product p, int position) {

        Product product = products.get(position);
        product.setName(p.getName());
        product.setCategory(p.getCategory());
        product.setPrice(p.getPrice());

        int i = db.updateProduct(product);
        Log.i("chk", "update return->" + i);
        if(i==1){
            mProductAdapter.notifyDataSetChanged();
        }
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
        Log.i("chk", "test onClick Override product activity " + position);
    }

    @Override
    public void onLongClick(View view, int position) {
        Log.i("chk", "test onLongClick Override product activity" + position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private IClickListener clicklistener;
        private GestureDetector gestureDetector;

        RecyclerTouchListener(Context context, final RecyclerView recycleView, final IClickListener clicklistener) {
            this.clicklistener = clicklistener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
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
