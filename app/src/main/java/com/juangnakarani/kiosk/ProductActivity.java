package com.juangnakarani.kiosk;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juangnakarani.kiosk.adapter.ProductAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Category;
import com.juangnakarani.kiosk.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
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

        mProductAdapter = new ProductAdapter(products);
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
            editTextProductID.setText(product.getId());
            editTextProductName.setText(product.getName());
            editTextProductPrice.setText(String.valueOf(product.getPrice()));
            editTextProductCategory.setText(product.getCategory().getDescription());
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

                // check if user updating note
                if (shouldUpdate && product != null) {
                    // update note by it's id
//                    updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
//                    createNote(inputNote.getText().toString());
                    int id = Integer.valueOf(editTextProductID.getText().toString());
                    String name = editTextProductName.getText().toString();
                    BigDecimal price = BigDecimal.valueOf(Integer.valueOf(editTextProductPrice.getText().toString()));
                    Category category = db.getCategoryByID(Integer.valueOf(editTextProductCategory.getText().toString()));
                    Product p = new Product(id, name, price, 0, category);
                    createProduct(p);
                }
            }
        });
    }

    private void createProduct(Product p) {
        long id = db.insertProduct(p);

        Product product = db.getProductByID(p.getId());
        if(product != null){
            products.add(p);
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

}
