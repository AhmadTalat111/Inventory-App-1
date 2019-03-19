package com.example.android.mybooks;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.example.android.mybooks.data.BookContract.BookEntry;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int BOOK_LOADER = 0;
    BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView bookListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }


    private void insertBook() {
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, "Beauty");
        values.put(BookEntry.COLUMN_BOOK_PRICE, 50);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 25);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, BookEntry.SUPPLIER_NASHRON);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, 1234567);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
    return new CursorLoader(this, BookEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
