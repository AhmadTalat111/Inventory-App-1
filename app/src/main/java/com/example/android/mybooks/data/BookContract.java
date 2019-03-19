package com.example.android.mybooks.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class BookContract {

    //constructor
    public BookContract() {
    }
    public static final String CONTENT_AUTHORITY = "com.example.android.mybooks";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "Books";


    public final static class BookEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;


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

        public static boolean isValidSupplierName(int suppliername) {
            if (suppliername == SUPPLIER_UNKNOWN || suppliername == SUPPLIER_JARIRR || suppliername == SUPPLIER_NASHRON) {
                return true;
            }
            return false;
        }
    }
}
