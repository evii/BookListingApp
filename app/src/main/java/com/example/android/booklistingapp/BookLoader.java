package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

/**
 * Created by evi on 22. 6. 2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Books>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

       @Override
    protected void onStartLoading() {
        forceLoad();
    }
    //This is on a background thread.
    @Override
    public List<Books> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Books> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }
}
