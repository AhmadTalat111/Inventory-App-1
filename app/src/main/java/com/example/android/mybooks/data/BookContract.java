package com.example.android.mybooks.data;

import android.provider.BaseColumns;


public class BookContract {

    //constructor
    public BookContract() {
    }

    public final static class BookEntry implements BaseColumns {

        //Name of table DB
        public final static String TABLE_NAME = "Books";

        //Column of table
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_BOOK_NAME = "book_name";
        public final static String COLUMN_BOOK_PRICE = "price";
        public final static String COLUMN_BOOK_QUANTITY = "quantity";
        public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        // SUPPLIER_NAME LIST VALUES
        public final static int SUPPLIER_UNKNOWN = 0;
        public final static int SUPPLIER_JARIRR = 1;
        public final static int SUPPLIER_NASHRON = 2;


    }
}
