package com.example.android.booklistingapp;

/**
 * Created by evi on 22. 6. 2017.
 */

public class Books {

    private String mTitle;
    private String mAuthor;
    private String mUrl;

    /**
     * Constructs a new {@link Books} object.
     *
     * @param title is the title of the book
     * @param author is the author of the book
     * @param url is the website URL to find more details about the book
     */
    public Books(String title, String author, String url) {
        mTitle = title;
        mAuthor = author;
        mUrl = url;
    }

    //Returns the title of the book.
    public String getTitle() {
        return mTitle;
    }

    //Returns the author of the book.
    public String getAuthor() {
        return mAuthor;
    }

    //Returns the website URL to find more information about the earthquake.
    public String getUrl() {
        return mUrl;
    }
}
