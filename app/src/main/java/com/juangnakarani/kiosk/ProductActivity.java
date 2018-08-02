package com.juangnakarani.kiosk;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.juangnakarani.kiosk.adapter.ProductAdapter;
import com.juangnakarani.kiosk.database.DatabaseContract;
import com.juangnakarani.kiosk.database.ProductDbHelper;
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

    ProductDbHelper productDbHelper;

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

        productDbHelper = new ProductDbHelper(getApplicationContext());
        // Gets the data repository in write mode
        products.addAll(productDbHelper.getAllProducts());

        mProductAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });
    }

    private void showNoteDialog(final boolean shouldUpdate, final Product product, final int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
