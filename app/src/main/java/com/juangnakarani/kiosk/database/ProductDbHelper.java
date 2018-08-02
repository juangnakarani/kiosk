package com.juangnakarani.kiosk.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.juangnakarani.kiosk.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Product.db";

    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE " + DatabaseContract.ProductEntity.TABLE_NAME + " (" +
                    DatabaseContract.ProductEntity.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.ProductEntity.COLUMN_NAME + " TEXT," +
                    DatabaseContract.ProductEntity.COLUMN_UNIT_PRICE + " INTEGER," +
                    DatabaseContract.ProductEntity.COLUMN_QTY_ORDERED + " INTEGER," +
                    DatabaseContract.ProductEntity.COLUMN_CATEGORY + " TEXT," +
                    DatabaseContract.ProductEntity.COLUMN_DATE_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String SQL_DELETE_PRODUCT =
            "DROP TABLE IF EXISTS " + DatabaseContract.ProductEntity.TABLE_NAME;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Product> getAllProducts(){
        Log.i("chkDb","getAllProducts ProductDbHelper");
        List<Product> products = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DatabaseContract.ProductEntity.TABLE_NAME + " ORDER BY " + DatabaseContract.ProductEntity.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntity.COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntity.COLUMN_NAME)));
                product.setPrice(BigDecimal.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntity.COLUMN_UNIT_PRICE))));

                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return products;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("chkDb","onCreate ProductDbHelper");

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.i("chkDb","onUpgrade ProductDbHelper");

        sqLiteDatabase.execSQL(SQL_DELETE_PRODUCT);
        onCreate(sqLiteDatabase);
    }
}
