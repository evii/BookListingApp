package com.example.android.booklistingapp;

/**
 * Created by evi on 22. 6. 2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.author;

/**
 * Helper methods related to requesting and receiving book data from Google Books.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    //Creates a private constructor
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Books} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Books> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Books> books = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            // Extract the JSONArray associated with the key called "items",
            // which represents a list of books.
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");


            //  Loop through each item in the array
            for (int i = 0; i < bookArray.length(); i++) {

                // Get book JSONObject at position i
                JSONObject currentBook = bookArray.getJSONObject(i);

                // Get “volumeInfo” JSONObject
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract “Author” for book
                String author = "";
                boolean authorExists = volumeInfo.has("authors");
                if (authorExists) {
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    author = authorsArray.getString(0);
                    for (int j = 1; j < authorsArray.length(); j++) {
                        String coauthor = authorsArray.getString(j);
                        author = author + ", " + coauthor;
                    }
                }

                // Extract “Title” for book
                String title = "";
                boolean titleExists = volumeInfo.has("title");
                if (titleExists) {
                title = volumeInfo.getString("title");
                }

                // Extract the value for the key called "url" - book detail info
                String url = "";
                boolean urlExists = volumeInfo.has("infoLink");
                if (urlExists) {
                url = volumeInfo.getString("infoLink"); }

                // Extract the value for the key called "smallThumbnail" - book cover image
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String pictureUrl = "";
                boolean pictureUrlExists = imageLinks.has("smallThumbnail");
                if(pictureUrlExists){
                pictureUrl = imageLinks.getString("smallThumbnail");}

                // Create a new {@link Book} object with the title, author and url
                // from the JSON response.
                // Add book to list of books
                books.add(new Books(title, author, url, pictureUrl));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the google books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the Google Books dataset and return an object to represent a single book.
     */
    public static List<Books> fetchBookData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Books> books = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return books;
    }


}

