package com.example.android.mybooks.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;




public class BookProvider extends ContentProvider {

    private static final int BOOKS = 100;

    private static final int BOOK_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);

        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper((getContext()));
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

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

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI" + uri + " with match " + match);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        String nameBook = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_NAME);
        if (nameBook == null) {
            throw new IllegalArgumentException("Book name requires");
        }

        Integer bookPrice = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        if (bookPrice != null && bookPrice < 0) {
            throw new IllegalArgumentException("Book price requires valid");
        }

        Integer quantityBook = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        if (quantityBook != null && quantityBook < 0) {
            throw new IllegalArgumentException("Book quantity requires valid");
        }

        Integer supplierName = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        if (supplierName == null || !BookContract.BookEntry.isValidSupplierName(supplierName)) {
            throw new IllegalArgumentException("Book requires valid supplier");
        }

        Integer supplierPhone = values.getAsInteger(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Book Phone requires valid");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(BookContract.BookEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.v("message:", "Failed to insert new row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
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
    public int update(Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_NAME)) {
            String nameBook = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_NAME);
            if (nameBook == null) {
                throw new IllegalArgumentException("Book name requires");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_PRICE)) {
            Integer priceBook = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PRICE);
            if (priceBook != null && priceBook < 0) {
                throw new
                        IllegalArgumentException("Book price requires valid");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_QUANTITY)) {
            Integer quantityBook = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
            if (quantityBook != null && quantityBook < 0) {
                throw new
                        IllegalArgumentException("Book quantity requires valid");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME)) {
            Integer supplierName = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            if (supplierName == null || !BookContract.BookEntry.isValidSupplierName(supplierName)) {
                throw new IllegalArgumentException("Supplier Name requires valid");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            Integer supplierPhone = values.getAsInteger(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplierPhone != null && supplierPhone < 0) {
                throw new
                        IllegalArgumentException("Supplier Phone requires valid");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}