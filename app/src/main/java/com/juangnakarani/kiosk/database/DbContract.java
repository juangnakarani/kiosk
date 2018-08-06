package com.juangnakarani.kiosk.database;

import android.provider.BaseColumns;

public class DbContract {


    /* Inner class that defines the table contents */
    public static class ProductEntity implements BaseColumns {
        public static final String TABLE_NAME = "t_product";
        public static final String COL_ID = "id";
        public static final String COL_NAME = "name";
        public static final String COL_PRICE = "price";
        public static final String COL_ORDERED = "ordered";
        public static final String COL_CATEGORY_ID = "category_id";
        public static final String COL_DATE_CREATED = "date_created";

    }

    public static class CategoryEntity implements BaseColumns{
        public static final String TABLE_NAME = "t_category";
        public static final String COL_ID = "id";
        public static final String COL_DESCRIPTION = "description";
    }

    public static class ApplicationStateEntity implements BaseColumns{
        public static final String TABLE_NAME = "t_application_state";
        public static final String COL_KEY = "key";
        public static final String COL_VALUE = "value";
    }

    public static class TransactionHeaderEntity implements BaseColumns{
        public static final String TABLE_NAME = "t_transaction_header";
        public static final String COL_ID = "id";
        public static final String COL_DATETIME = "datetime";
        public static final String COL_TOTAL = "total";
        public static final String COL_RECEIVED = "received";
    }

    public static class TransactionDetailEntity implements BaseColumns{
        public static final String TABLE_NAME = "t_transaction_detail";
        public static final String COL_ID = "id";
        public static final String COL_TRANSACTION_ID = "transaction_id";
        public static final String COL_PRODUCT_ID = "product_id";
        public static final String COL_ORDERED = "ordered";
        public static final String COL_CATEGORY_ID = "category_id";
        public static final String COL_PRICE = "price";
    }

    private DbContract() {
    }
}
