package com.juangnakarani.kiosk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.juangnakarani.kiosk.adapter.ProductAdapter;
import com.juangnakarani.kiosk.adapter.TransactionAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Product;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTrasactionAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> products = new ArrayList<>();
    private DbHelper db;
    private TextView mTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rclv_transaction);
        mRecyclerView.setHasFixedSize(true);

        mTrasactionAdapter = new TransactionAdapter(products);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTrasactionAdapter);

        products.clear();
        db = new DbHelper(getApplicationContext());
        // Gets the data repository in write mode
        products.addAll(db.getOrdered());
        mTrasactionAdapter.notifyDataSetChanged();

        Log.i("chk", "must calculated now.....");
        mTotal = findViewById(R.id.total_amount);
        int total = db.calculateTotal();
        Log.i("chk", "total is " + total);

        mTotal.setText("Total: " + total);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
