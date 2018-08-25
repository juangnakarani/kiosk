package com.juangnakarani.kiosk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;

import com.juangnakarani.kiosk.model.Category;
import com.juangnakarani.kiosk.model.Product;
import com.juangnakarani.kiosk.model.TransactionDetail;
import com.juangnakarani.kiosk.model.TransactionHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Kiosk.db";

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

    private static final String SQL_CREATE_APPLICATION_STATE = "CREATE TABLE " + DbContract.ApplicationStateEntity.TABLE_NAME + "( " +
            DbContract.ApplicationStateEntity.COL_KEY + " TEXT PRIMARY KEY," +
            DbContract.ApplicationStateEntity.COL_VALUE + " INTEGER)";

    private static final String SQL_INSERT_TRANSACTION_RESULT_STATE = "INSERT INTO " + DbContract.ApplicationStateEntity.TABLE_NAME + "(" + DbContract.ApplicationStateEntity.COL_KEY + " , " + DbContract.ApplicationStateEntity.COL_VALUE + ") VALUES ('key_transaction_status', 0)";

    private static final String SQL_DROP_APPLICATION_STATE = "DROP TABLE IF EXISTS " + DbContract.ApplicationStateEntity.TABLE_NAME;

    private static final String SQL_CREATE_TRANSACTION_HEADER = "CREATE TABLE " + DbContract.TransactionHeaderEntity.TABLE_NAME + " (" +
            DbContract.TransactionHeaderEntity.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DbContract.TransactionHeaderEntity.COL_DATETIME + " TEXT," +
            DbContract.TransactionHeaderEntity.COL_TOTAL + " INTEGER," +
            DbContract.TransactionHeaderEntity.COL_RECEIVED + " INTEGER)";

    private static final String SQL_CREATE_TRANSACTION_DETAIL = "CREATE TABLE " + DbContract.TransactionDetailEntity.TABLE_NAME + " (" +
            DbContract.TransactionDetailEntity.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DbContract.TransactionDetailEntity.COL_TRANSACTION_ID + " INTEGER," +
            DbContract.TransactionDetailEntity.COL_PRODUCT_ID + " INTEGER," +
            DbContract.TransactionDetailEntity.COL_CATEGORY_ID + " INTEGER," +
            DbContract.TransactionDetailEntity.COL_ORDERED + " INTEGER," +
            DbContract.TransactionDetailEntity.COL_PRICE + " INTEGER)";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        Log.i("chkDb", "onCreate ProductDbHelper");

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY);
        sqLiteDatabase.execSQL(SQL_CREATE_APPLICATION_STATE);
        sqLiteDatabase.execSQL(SQL_INSERT_TRANSACTION_RESULT_STATE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRANSACTION_HEADER);
        sqLiteDatabase.execSQL(SQL_CREATE_TRANSACTION_DETAIL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        Log.i("chkDb", "onUpgrade ProductDbHelper");

        sqLiteDatabase.execSQL(SQL_DROP_PRODUCT);
        sqLiteDatabase.execSQL(SQL_DROP_CATEGORY);
        sqLiteDatabase.execSQL(SQL_DROP_APPLICATION_STATE);
        onCreate(sqLiteDatabase);
    }

    public long insertTransactionHeader(TransactionHeader th) {
//        Log.i("chk", "insertTransactionHeader");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.TransactionHeaderEntity.COL_DATETIME, th.getDatetime());
        values.put(DbContract.TransactionHeaderEntity.COL_TOTAL, th.getTotal());
        values.put(DbContract.TransactionHeaderEntity.COL_RECEIVED, th.getReceived());

        long id = db.insert(DbContract.TransactionHeaderEntity.TABLE_NAME, null, values);
        //db.close();
        return id;
    }

    public int getTrasactionState() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DbContract.ApplicationStateEntity.TABLE_NAME, new String[]{DbContract.ApplicationStateEntity.COL_KEY, DbContract.ApplicationStateEntity.COL_VALUE,}, DbContract.ApplicationStateEntity.COL_KEY + "=?",
                new String[]{String.valueOf("key_transaction_status")}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex(DbContract.ApplicationStateEntity.COL_VALUE));
    }

    public int setTransactionState(int s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ApplicationStateEntity.COL_VALUE, s);

        return db.update(DbContract.ApplicationStateEntity.TABLE_NAME, values, DbContract.ApplicationStateEntity.COL_KEY + " = ?",
                new String[]{String.valueOf("key_transaction_status")});
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

    public int updateProduct(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ProductEntity.COL_NAME, p.getName());
        values.put(DbContract.ProductEntity.COL_CATEGORY_ID, p.getCategory().getId());
        values.put(DbContract.ProductEntity.COL_PRICE, String.valueOf(p.getPrice()));

        int update = db.update(DbContract.ProductEntity.TABLE_NAME, values, DbContract.ProductEntity.COL_ID + " =?", new String[]{String.valueOf(p.getId())});
        return update;
    }

    public List<Product> getAllProducts() {
//        Log.i("chkDb", "getAllProducts ProductDbHelper");
        List<Product> products = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DbContract.ProductEntity.TABLE_NAME + " ORDER BY " + DbContract.ProductEntity.COL_ID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(DbContract.ProductEntity.COL_NAME)));
                product.setPrice(BigDecimal.valueOf(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE))));
                product.setOrdered(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED)));
                product.setCategory(getCategoryByID(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_CATEGORY_ID))));
                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
//        db.close();

        return products;
    }

    public List<Product> getProductsByCategory(int id) {
//        Log.i("chkDb", "getAllProducts ProductDbHelper");
        List<Product> products = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DbContract.ProductEntity.TABLE_NAME + " WHERE " + DbContract.ProductEntity.COL_CATEGORY_ID + "=" + id + " ORDER BY " + DbContract.ProductEntity.COL_ID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(DbContract.ProductEntity.COL_NAME)));
                product.setPrice(BigDecimal.valueOf(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE))));
                product.setOrdered(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED)));
                product.setCategory(getCategoryByID(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_CATEGORY_ID))));
                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
//        db.close();

        return products;
    }

    public List<Product> getOrdered() {
//        Log.i("chkDb", "getAllProducts ProductDbHelper");
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
                product.setCategory(getCategoryByID(cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_CATEGORY_ID))));
                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
//        db.close();

        return products;
    }

    public long insertCategory(Category c) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.CategoryEntity.COL_ID, c.getId());
        values.put(DbContract.CategoryEntity.COL_DESCRIPTION, c.getDescription());

        long id = db.insert(DbContract.CategoryEntity.TABLE_NAME, null, values);

        return id;

    }

    public void installDefaultData() {
        Category food = new Category(1, "Bakso");
        this.insertCategory(food);

        Category beverage = new Category(2, "Minuman");
        this.insertCategory(beverage);

        Category other = new Category(3, "Lain-lain");
        this.insertCategory(other);
        //  food
        Product baksoBakar = new Product(101, "Bakso Bakar", BigDecimal.valueOf(12000), 0, this.getCategoryByID(1));
        this.insertProduct(baksoBakar);

        Product baksoSolo = new Product(102, "Bakso Solo", BigDecimal.valueOf(12000), 0, this.getCategoryByID(1));
        this.insertProduct(baksoSolo);

        Product pentolKasarBesar = new Product(103, "Pentol Kasar Besar", BigDecimal.valueOf(4000), 0, this.getCategoryByID(1));
        this.insertProduct(pentolKasarBesar);

        Product pentolHalus = new Product(104, "Pentol Halus", BigDecimal.valueOf(2000), 0, this.getCategoryByID(1));
        this.insertProduct(pentolHalus);

        Product pentolGoreng = new Product(105, "Pentol Goreng", BigDecimal.valueOf(2000), 0, this.getCategoryByID(1));
        this.insertProduct(pentolGoreng);

        Product tahu = new Product(106, "Tahu", BigDecimal.valueOf(2000), 0, this.getCategoryByID(1));
        this.insertProduct(tahu);

        Product pangsitGoreng = new Product(107, "Pangsit Goreng", BigDecimal.valueOf(2000), 0, this.getCategoryByID(1));
        this.insertProduct(pangsitGoreng);

        // beverage
        Product esOyen = new Product(201, "Es Oyen", BigDecimal.valueOf(6000), 0, this.getCategoryByID(2));
        this.insertProduct(esOyen);

        Product esTeh = new Product(202, "Es Teh", BigDecimal.valueOf(3000), 0, this.getCategoryByID(2));
        this.insertProduct(esTeh);

        Product tehHangat = new Product(203, "Teh Hangat", BigDecimal.valueOf(3000), 0, this.getCategoryByID(2));
        this.insertProduct(tehHangat);

        Product esJeruk = new Product(204, "Es Jeruk", BigDecimal.valueOf(4000), 0, this.getCategoryByID(2));
        this.insertProduct(esJeruk);

        Product jerukHangat = new Product(205, "Jeruk Hangat", BigDecimal.valueOf(4000), 0, this.getCategoryByID(2));
        this.insertProduct(jerukHangat);

        Product airMineral = new Product(206, "Air Mineral", BigDecimal.valueOf(3000), 0, this.getCategoryByID(2));
        this.insertProduct(airMineral);

        // others
        Product lontong = new Product(301, "Lontong", BigDecimal.valueOf(2000), 0, this.getCategoryByID(3));
        this.insertProduct(lontong);

        Product kerupuk = new Product(302, "Kerupuk", BigDecimal.valueOf(1000), 0, this.getCategoryByID(3));
        this.insertProduct(kerupuk);
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
//        db.close();
//        Log.i("chk categories", String.valueOf(categories.get(0).getDescription()));
        return categories;

    }

    public Category getCategoryByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DbContract.CategoryEntity.TABLE_NAME, new String[]{DbContract.CategoryEntity.COL_ID, DbContract.CategoryEntity.COL_DESCRIPTION},
                DbContract.ProductEntity.COL_ID + " =?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        //instance new product
        Category c = new Category(
                cursor.getInt(cursor.getColumnIndex(DbContract.CategoryEntity.COL_ID)),
                cursor.getString(cursor.getColumnIndex(DbContract.CategoryEntity.COL_DESCRIPTION)));

//        db.close();
        return c;
    }

    public int incrementOrder(int id) {

        Product p = this.getProductByID(id);
        int ordered = p.getOrdered() + 1;

//        Log.i("chkDb", "incrementOrder " + id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ProductEntity.COL_ORDERED, ordered);

        return db.update(DbContract.ProductEntity.TABLE_NAME, values, DbContract.ProductEntity.COL_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public int decrementOrder(int id) {
        int ordered;
        Product p = this.getProductByID(id);
        if (p.getOrdered() > 0) {
            ordered = p.getOrdered() - 1;
        } else {
            return 0;
        }

//        Log.i("chkDb", "incrementOrder " + id);
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
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ID));
            int price = cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_PRICE));
            int ordered = cursor.getInt(cursor.getColumnIndex(DbContract.ProductEntity.COL_ORDERED));
            int unitTotal = price * ordered;
            total = total + unitTotal;
//            Log.i("chk", "total " + id + "->" + unitTotal);
        }

        return total;
    }

    public int clearTransaction() {
//        Log.i("chkDb", "clearTransaction ");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ProductEntity.COL_ORDERED, 0);

        return db.update(DbContract.ProductEntity.TABLE_NAME, values, null, null);
    }

    public List<TransactionHeader> getAllTransactionHeader() {
        List<TransactionHeader> transactionHeaders = new ArrayList<>();

        //query select
        String query = "SELECT * FROM " + DbContract.TransactionHeaderEntity.TABLE_NAME + " ORDER BY " + DbContract.TransactionHeaderEntity.COL_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransactionHeader th = new TransactionHeader();
                th.setId(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionHeaderEntity.COL_ID)));
                th.setDatetime(cursor.getString(cursor.getColumnIndex(DbContract.TransactionHeaderEntity.COL_DATETIME)));
                th.setTotal(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionHeaderEntity.COL_TOTAL)));
                th.setReceived(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionHeaderEntity.COL_RECEIVED)));

                transactionHeaders.add(th);
            } while (cursor.moveToNext());
        }

        // close db connection
        // db.close();
        // Log.i("chk", "getAllTransactionHeader size: " + transactionHeaders.size());
        return transactionHeaders;
    }

    public long insertTransactionDetail(long th_id, Product p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.TransactionDetailEntity.COL_TRANSACTION_ID, th_id);
        values.put(DbContract.TransactionDetailEntity.COL_CATEGORY_ID, p.getCategory().getId());
        values.put(DbContract.TransactionDetailEntity.COL_PRODUCT_ID, p.getId());
        values.put(DbContract.TransactionDetailEntity.COL_ORDERED, p.getOrdered());

        long id = db.insert(DbContract.TransactionDetailEntity.TABLE_NAME, null, values);

        return id;
    }

    public List<TransactionDetail> getTransactionDetailByID(long id) {
        List<TransactionDetail> transactionDetails = new ArrayList<>();
        //query select
        String query = "SELECT * FROM " + DbContract.TransactionDetailEntity.TABLE_NAME + " WHERE " + DbContract.TransactionDetailEntity.COL_TRANSACTION_ID + " = " + id + " ORDER BY " + DbContract.TransactionHeaderEntity.COL_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransactionDetail td = new TransactionDetail();
                td.setId(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionDetailEntity.COL_ID)));
                td.setTransactionID(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionDetailEntity.COL_TRANSACTION_ID)));
                td.setProductID(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionDetailEntity.COL_PRODUCT_ID)));
                td.setCategoryID(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionDetailEntity.COL_CATEGORY_ID)));
                td.setOrdered(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionDetailEntity.COL_ORDERED)));
                td.setPrice(cursor.getInt(cursor.getColumnIndex(DbContract.TransactionDetailEntity.COL_PRICE)));

                transactionDetails.add(td);
            } while (cursor.moveToNext());
        }

        // close db connection
        // db.close();
        // Log.i("chk", "getTransactionDetailByID size: " + transactionDetails.size());
        return transactionDetails;
    }
}
