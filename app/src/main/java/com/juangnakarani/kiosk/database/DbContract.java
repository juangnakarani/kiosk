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
        public static final String COL_CATEGORY_ID = "category_ID";
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

    private DbContract() {
    }
}
