package com.example.android.mybooks;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.mybooks.data.BookContract;


public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        TextView productNameTextView = view.findViewById(R.id.name);
        TextView productPriceTextView = view.findViewById(R.id.price);
        TextView productQuantityTextView = view.findViewById(R.id.quantity);
        Button productSaleButton = view.findViewById(R.id.btnSale);

        final int columnIdIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);

        final String productID = cursor.getString(columnIdIndex);
        String productName = cursor.getString(productNameColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);
        final String productQuantity = cursor.getString(productQuantityColumnIndex);

        productSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogActivity Activity = (CatalogActivity) context;
                Activity.bookSaleCount(Integer.valueOf(productID), Integer.valueOf(productQuantity));
            }
        });

        productNameTextView.setText(productID + " ) " + productName);
        productPriceTextView.setText(context.getString(R.string.book_price) + " : " + productPrice + "  " + context.getString(R.string.book_price_currency));
        productQuantityTextView.setText(context.getString(R.string.book_quantity) + " : " + productQuantity);

        Button productEditButton = view.findViewById(R.id.btnEdit);
        productEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditorActivity.class);
                Uri currentProdcuttUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, Long.parseLong(productID));
                intent.setData(currentProdcuttUri);
                context.startActivity(intent);
            }
        });

    }

}
