package com.example.android.booklistingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


import static android.R.attr.author;
import static com.example.android.booklistingapp.QueryUtils.createUrl;

/**
 * Created by evi on 22. 6. 2017.
 */

public class BookAdapter extends ArrayAdapter<Books> {

    public BookAdapter(Activity context, ArrayList<Books> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Find the book at the given position in the list of books
        Books currentBook = getItem(position);

        // Find the TextView with view ID author
        TextView authorView = (TextView) listItemView.findViewById(R.id.author_view);

        // Display the author of the current book in that TextView
        String author = currentBook.getAuthor();
        authorView.setText(author);

        // Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title_view);

        // Display the title of the current book in that TextView
        String title = currentBook.getTitle();
        titleView.setText(title);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
