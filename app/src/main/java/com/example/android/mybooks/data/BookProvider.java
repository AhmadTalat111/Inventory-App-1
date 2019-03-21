package com.example.android.mybooks.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.android.mybooks.data.BookContract.BookEntry;
import static com.example.android.mybooks.data.BookContract.CONTENT_AUTHORITY;
import static com.example.android.mybooks.data.BookContract.PATH_BOOKS;


public class BookProvider extends ContentProvider {

    public static final String LOG_TAG = BookProvider.class.getSimpleName();

   private BookDbHelper mDbHelper;
    private static final int BOOKS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int BOOK_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_BOOKS, BOOKS);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.app.provider/table3/3" matches, but
         * "content://com.example.app.provider/table3 doesn't.
         */
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_BOOKS + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
       mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,null, null,
                        sortOrder);
                break;
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

        private Uri insertBook(Uri uri, ContentValues values) {
        String bookName = values.getAsString(BookEntry.COLUMN_BOOK_NAME);
        if (bookName == null) {
            throw new IllegalArgumentException("Book name is required");
        }

        Integer bookPrice = values.getAsInteger(BookEntry.COLUMN_BOOK_PRICE);
        if (bookPrice != null && bookPrice < 0) {
            throw new IllegalArgumentException("Product price requires valid");
        }

        Integer bookQuantity = values.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY);
        if (bookQuantity != null && bookQuantity < 0) {
            throw new IllegalArgumentException("Book quantity must be valid");
        }

        Integer supplierName = values.getAsInteger(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        if (supplierName == null || !BookEntry.isValidSupplierName(supplierName)) {
            throw new IllegalArgumentException("Book supplier name is required");
        }

        Integer supplierPhone = values.getAsInteger(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Supplier Phone must be valid");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(BookEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String bookName = values.getAsString(BookEntry.COLUMN_BOOK_NAME);
        if (bookName == null) {
            throw new IllegalArgumentException("Book name is required");
        }

        Integer bookPrice = values.getAsInteger(BookEntry.COLUMN_BOOK_PRICE);
        if (bookPrice != null && bookPrice < 0) {
            throw new IllegalArgumentException("Product price requires valid");
        }

        Integer bookQuantity = values.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY);
        if (bookQuantity != null && bookQuantity < 0) {
            throw new IllegalArgumentException("Book quantity must be valid");
        }

        Integer supplierName = values.getAsInteger(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        if (supplierName == null || !BookEntry.isValidSupplierName(supplierName)) {
            throw new IllegalArgumentException("Book supplier name is required");
        }

        Integer supplierPhone = values.getAsInteger(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Supplier Phone must be valid");
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}