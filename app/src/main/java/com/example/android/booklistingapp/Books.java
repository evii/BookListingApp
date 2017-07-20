package com.example.android.booklistingapp;

/**
 * Created by evi on 22. 6. 2017.
 */

final public class Books {

    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private String mPictureUrl;

    /**
     * Constructs a new {@link Books} object.
     *
     * @param title  is the title of the book
     * @param author is the author of the book
     * @param url    is the website URL to find more details about the book
     */
    public Books(String title, String author, String url, String pictureUrl) {
        mTitle = title;
        mAuthor = author;
        mUrl = url;
        mPictureUrl = pictureUrl;
    }

    //Returns the title of the book.
    public String getTitle() {
        return mTitle;
    }

    //Returns the author of the book.
    public String getAuthor() {
        return mAuthor;
    }

    //Returns the website URL to find more information about the book.
    public String getUrl() {
        return mUrl;
    }

    // Returns url for picture of the cover of the book
    public String getPictureUrl() {
        return mPictureUrl;
    }
}
