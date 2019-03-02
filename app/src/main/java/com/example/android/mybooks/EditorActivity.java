package com.example.android.mybooks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mybooks.data.BookContract;
import com.example.android.mybooks.data.BookDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private Spinner mSupplierSpinner;
    private EditText mSupplierPhoneEditText;


    private int mSupplier = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_book_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_book_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_book_quantity);
        mSupplierSpinner = (Spinner) findViewById(R.id.spinner_supplier);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);

        setupSpinner();
    }


    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_jarir))) {
                        mSupplier = 1;
                    } else if (selection.equals(getString(R.string.supplier_Nashron))) {
                        mSupplier = 2;
                    } else {
                        mSupplier = 0;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = 0; // Unknown
            }
        });
    }

    private void insertProduct() {
        String bookNameString = mNameEditText.getText().toString().trim();

        String bookPriceString = mPriceEditText.getText().toString().trim();
        int bookPriceInteger = Integer.parseInt(bookPriceString);

        String bookQuantityString = mQuantityEditText.getText().toString().trim();
        int bookQuantityInteger = Integer.parseInt(bookQuantityString);

        String bookSupplierPhoneNumberString = mSupplierPhoneEditText.getText().toString().trim();
        int supplierPhoneNumberInteger = Integer.parseInt(bookSupplierPhoneNumberString);

        BookDbHelper mDbHelper = new BookDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, bookNameString);
        values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, bookPriceInteger);
        values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, bookQuantityInteger);
        values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME, mSupplier);
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumberInteger);

        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
            Log.d("Error message", "Doesn't insert row on table");

        } else {
            Toast.makeText(this, "Product saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
            Log.d("successfully message", "insert row on table");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
