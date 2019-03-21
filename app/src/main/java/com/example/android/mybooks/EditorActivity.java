package com.example.android.mybooks;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mybooks.data.BookContract;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private EditText mProductNameEditText;
    private EditText mProductPriceEditText;
    private EditText mProductQuantityEditText;
    private Spinner mProductSupplieNameSpinner;
    private EditText mProductSupplierPhoneNumberEditText;

    private int mSupplieName = BookContract.BookEntry.SUPPLIER_UNKNOWN;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.add_book));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        mProductNameEditText = findViewById(R.id.edit_book_name);
        mProductPriceEditText = findViewById(R.id.edit_book_price);
        mProductQuantityEditText = findViewById(R.id.edit_book_quantity);
        mProductSupplieNameSpinner = findViewById(R.id.spinner_supplier);
        mProductSupplierPhoneNumberEditText = findViewById(R.id.edit_supplier_phone);

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mProductPriceEditText.setOnTouchListener(mTouchListener);
        mProductQuantityEditText.setOnTouchListener(mTouchListener);
        mProductSupplieNameSpinner.setOnTouchListener(mTouchListener);
        mProductSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter productSupplieNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        productSupplieNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mProductSupplieNameSpinner.setAdapter(productSupplieNameSpinnerAdapter);

        mProductSupplieNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    mSupplieName = BookContract.BookEntry.SUPPLIER_JARIRR;
                } else if (position == 2) {
                    mSupplieName = BookContract.BookEntry.SUPPLIER_NASHRON;
                } else {
                    mSupplieName = BookContract.BookEntry.SUPPLIER_UNKNOWN;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplieName = BookContract.BookEntry.SUPPLIER_UNKNOWN;
            }
        });
    }


    private void saveProduct() {
        String productNameString = mProductNameEditText.getText().toString().trim();
        String productPriceString = mProductPriceEditText.getText().toString().trim();
        String productQuantityString = mProductQuantityEditText.getText().toString().trim();
        String productSupplierPhoneNumberString = mProductSupplierPhoneNumberEditText.getText().toString().trim();
        if (mCurrentProductUri == null) {
            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mSupplieName == BookContract.BookEntry.SUPPLIER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_requires), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, productNameString);
            values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, productPriceString);
            values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, productQuantityString);
            values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME, mSupplieName);
            values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, productSupplierPhoneNumberString);

            Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {

            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mSupplieName == BookContract.BookEntry.SUPPLIER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_requires), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, productNameString);
            values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, productPriceString);
            values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, productQuantityString);
            values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME, mSupplieName);
            values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, productSupplierPhoneNumberString);


            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        Log.d("message", "open Editor Activity");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_BOOK_NAME,
                BookContract.BookEntry.COLUMN_BOOK_PRICE,
                BookContract.BookEntry.COLUMN_BOOK_QUANTITY,
                BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mProductNameEditText.setText(currentName);
            mProductPriceEditText.setText(Integer.toString(currentPrice));
            mProductQuantityEditText.setText(Integer.toString(currentQuantity));
            mProductSupplierPhoneNumberEditText.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {

                case BookContract.BookEntry.SUPPLIER_JARIRR:
                    mProductSupplieNameSpinner.setSelection(1);
                    break;
                case BookContract.BookEntry.SUPPLIER_NASHRON:
                    mProductSupplieNameSpinner.setSelection(2);
                    break;
                default:
                    mProductSupplieNameSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductNameEditText.setText("");
        mProductPriceEditText.setText("");
        mProductQuantityEditText.setText("");
        mProductSupplierPhoneNumberEditText.setText("");
        mProductSupplieNameSpinner.setSelection(0);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
