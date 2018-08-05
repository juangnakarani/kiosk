package com.juangnakarani.kiosk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.juangnakarani.kiosk.model.Category;
import com.juangnakarani.kiosk.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Product.db";

    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE " + DbContract.ProductEntity.TABLE_NAME + " (" +
                    DbContract.ProductEntity.COL_ID + " INTEGER PRIMARY KEY," +
                    DbContract.ProductEntity.COL_NAME + " TEXT," +
                    DbContract.ProductEntity.COL_PRICE + " INTEGER," +
                    DbContract.ProductEntity.COL_ORDERED + " INTEGER," +
                    DbContract.ProductEntity.COL_CATEGORY_ID + " TEXT," +
                    DbContract.ProductEntity.COL_DATE_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String SQL_DROP_PRODUCT =
            "DROP TABLE IF EXISTS " + DbContract.ProductEntity.TABLE_NAME;

    private static final String SQL_CREATE_CATEGORY = "CREATE TABLE " + DbContract.CategoryEntity.TABLE_NAME + " (" +
            DbContract.CategoryEntity.COL_ID + " INTEGER PRIMARY KEY," +
            DbContract.CategoryEntity.COL_DESCRIPTION + " TEXT)";

    private static final String SQL_DROP_CATEGORY = "DROP TABLE IF EXISTS " + DbContract.CategoryEntity.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Product getProductByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DbContract.ProductEntity.TABLE_NAME, new String[]{DbContract.ProductEntity.COL_ID, DbContract.ProductEntity.COL_NAME,
                        DbContract.ProductEntity.COL_PRICE, DbContract.ProductEntity.COL_ORDERED, DbContract.ProductEntity.COL_CATEGORY_ID}, DbContract.ProductEntity.COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //instance new product
//        public Product(int id, String name, BigDecimal price, int ordered, Category category) {
        Product p = new Product(
                cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID)),
                cursor.getString(cursor.getColumnIndex(DbContract.ProductEntity.COL_NAME)),
                BigDecimal.valueOf(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE))),
                cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED)),
                new Category(Integer.valueOf(cursor.getColumnIndex(DbContract.ProductEntity.COL_CATEGORY_ID))));

        return p;
    }

    public long insertProduct(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ProductEntity.COL_ID, p.getId());
        values.put(DbContract.ProductEntity.COL_NAME, p.getName());
        values.put(DbContract.ProductEntity.COL_CATEGORY_ID, p.getCategory().getId());
        values.put(DbContract.ProductEntity.COL_PRICE, String.valueOf(p.getPrice()));

        long id = db.insert(DbContract.ProductEntity.TABLE_NAME, null, values);

        return id;

    }

    public List<Product> getAllProducts() {
        Log.i("chkDb", "getAllProducts ProductDbHelper");
        List<Product> products = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DbContract.ProductEntity.TABLE_NAME + " ORDER BY " + DbContract.ProductEntity.COL_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(DbContract.ProductEntity.COL_NAME)));
                product.setPrice(BigDecimal.valueOf(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE))));
                product.setOrdered(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return products;
    }

    public List<Product> getProductsByCategory(int id) {
        Log.i("chkDb", "getAllProducts ProductDbHelper");
        List<Product> products = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DbContract.ProductEntity.TABLE_NAME + " WHERE " + DbContract.ProductEntity.COL_CATEGORY_ID + "="+ id + " ORDER BY " + DbContract.ProductEntity.COL_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(DbContract.ProductEntity.COL_NAME)));
                product.setPrice(BigDecimal.valueOf(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE))));
                product.setOrdered(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return products;
    }

    public List<Product> getOrdered() {
        Log.i("chkDb", "getAllProducts ProductDbHelper");
        List<Product> products = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DbContract.ProductEntity.TABLE_NAME + " WHERE " + DbContract.ProductEntity.COL_ORDERED + " <>0" + " ORDER BY " + DbContract.ProductEntity.COL_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(DbContract.ProductEntity.COL_NAME)));
                product.setPrice(BigDecimal.valueOf(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE))));
                product.setOrdered(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return products;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("chkDb", "onCreate ProductDbHelper");

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.i("chkDb", "onUpgrade ProductDbHelper");

        sqLiteDatabase.execSQL(SQL_DROP_PRODUCT);
        sqLiteDatabase.execSQL(SQL_DROP_CATEGORY);
        onCreate(sqLiteDatabase);
    }

    public long insertCategory(Category c) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.CategoryEntity.COL_ID, c.getId());
        values.put(DbContract.CategoryEntity.COL_DESCRIPTION, c.getDescription());

        long id = db.insert(DbContract.CategoryEntity.TABLE_NAME, null, values);

        return id;

    }

    public void insertDefaultCategory(){
        Category food = new Category(1, "Bakso");
        this.insertCategory(food);

        Category beverage = new Category(2, "Minuman");
        this.insertCategory(beverage);

        Category other = new Category(3, "Lain-lain");
        this.insertCategory(other);
    }

    public List<Category> getAllCategory() {
        List<Category> categories = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DbContract.CategoryEntity.TABLE_NAME + " ORDER BY " + DbContract.CategoryEntity.COL_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(DbContract.CategoryEntity.COL_ID)));
                category.setDescription(cursor.getString(cursor.getColumnIndex(DbContract.CategoryEntity.COL_DESCRIPTION)));

                categories.add(category);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
//        Log.i("chk categories", String.valueOf(categories.get(0).getDescription()));
        return categories;

    }

    public Category getCategoryByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DbContract.CategoryEntity.TABLE_NAME, new String[]{DbContract.CategoryEntity.COL_ID, DbContract.CategoryEntity.COL_DESCRIPTION},
                DbContract.ProductEntity.COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //instance new product
        Category c = new Category(
                cursor.getInt(cursor.getColumnIndex(DbContract.CategoryEntity.COL_ID)),
                cursor.getString(cursor.getColumnIndex(DbContract.CategoryEntity.COL_DESCRIPTION)));

        return c;
    }

    public int incrementOrder(int id) {

        Product p = this.getProductByID(id);
        int ordered = p.getOrdered() + 1;

        Log.i("chkDb", "incrementOrder " + id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ProductEntity.COL_ORDERED, ordered);

        return db.update(DbContract.ProductEntity.TABLE_NAME, values, DbContract.ProductEntity.COL_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public int decrementOrder(int id) {
        int ordered;
        Product p = this.getProductByID(id);
        if(p.getOrdered()>0){
            ordered = p.getOrdered() - 1;
        }else{
            return 0;
        }

        Log.i("chkDb", "incrementOrder " + id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ProductEntity.COL_ORDERED, ordered);

        return db.update(DbContract.ProductEntity.TABLE_NAME, values, DbContract.ProductEntity.COL_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public int calculateTotal() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DbContract.ProductEntity.TABLE_NAME, new String[]{DbContract.ProductEntity.COL_ID, DbContract.ProductEntity.COL_ORDERED, DbContract.ProductEntity.COL_PRICE},
                DbContract.ProductEntity.COL_ORDERED + "<>?",
                new String[]{String.valueOf(0)}, null, null, null, null);

        int total = 0;
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID));
            int price = cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE));
            int ordered =  cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED));
            int unitTotal = price * ordered;
            total = total + unitTotal;
            Log.i("chk", "total " + id + "->" + unitTotal);
        }

        return total;
    }
}
