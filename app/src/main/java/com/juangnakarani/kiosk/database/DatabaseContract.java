package com.juangnakarani.kiosk.database;

import android.provider.BaseColumns;

public class DatabaseContract {


    /* Inner class that defines the table contents */
    public static class ProductEntity implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_UNIT_PRICE = "unit_price";
        public static final String COLUMN_QTY_ORDERED = "qty_ordered";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DATE_CREATED = "date_created";

    }

    private DatabaseContract() {
    }
}
