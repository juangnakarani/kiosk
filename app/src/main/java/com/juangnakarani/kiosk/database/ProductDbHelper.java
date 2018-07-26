package com.juangnakarani.kiosk.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Product.db";

    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " (" +
                    ProductContract.ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    ProductContract.ProductEntry.COLUMN_NAME + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_UNIT_PRICE + " INTEGER," +
                    ProductContract.ProductEntry.COLUMN_QTY_ORDERED + " INTEGER," +
                    ProductContract.ProductEntry.COLUMN_CATEGORY + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_TRANSACTION_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String SQL_DELETE_PRODUCT =
            "DROP TABLE IF EXISTS " + ProductContract.ProductEntry.TABLE_NAME;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_PRODUCT);
        onCreate(sqLiteDatabase);
    }
}
