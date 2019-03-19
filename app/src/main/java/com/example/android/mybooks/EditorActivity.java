package com.example.android.mybooks;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mybooks.data.BookContract.BookEntry;

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
        Intent intent = getIntent();
        Uri currentBookUri = intent.getData();

        if(currentBookUri == null){
            setTitle(R.string.editor_activity_title_new_book);
        } else {
            setTitle(R.string.editor_activity_title_edit_book);
        }

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



        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, bookNameString);
        values.put(BookEntry.COLUMN_BOOK_PRICE, bookPriceInteger);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantityInteger);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, mSupplier);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumberInteger);

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                    Toast.LENGTH_SHORT).show();
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
                insertProduct();
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
