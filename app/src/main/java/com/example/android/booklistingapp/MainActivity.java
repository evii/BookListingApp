package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Books>> {

    // Constant value for the book loader ID.
    private static final int BOOK_LOADER_ID = 1;

    // Adapter for the list of books
    private BookAdapter mAdapter;

    // String tag for log messages
    public static final String LOG_TAG = MainActivity.class.getName();

    // URL for book data from the Google Books dataset. adjusted url for search on Google books
    private String newGoogleBooksQueryUrl;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    //TODO: po otoceni, aby se nezvetsilo edittext pole
    // TODO: chyba pri prvnim nacteni - zkontrolovat
    //TODO: nacteni obrazku knizek implementovat
    // TODO: po kliknuti na enter v soft klavesnici - search


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting the URL for the default search displayed after loading app
        newGoogleBooksQueryUrl =  "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=20";

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Books>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                Books currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(websiteIntent);}
            }
        });

        // Check internet connection
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        // Get a reference to the LoaderManager, in order to interact with loaders.
        final LoaderManager loaderManager = getLoaderManager();

        // If there is a network connection, fetch data
        if(isConnected) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);}
        else {
            // Otherwise, display error
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

            // Hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progressbar_view);
            loadingIndicator.setVisibility(View.GONE);

       }

        /**
         * setting an onClickListener on the search button
         * appending the searched term to the default url
         * initiating the loader
         */
        final EditText searchTextView = (EditText) findViewById(R.id.search_view);
        Button searchButton =(Button) findViewById(R.id.search_button);
        final TextView keywordTextView = (TextView) findViewById(R.id.keyword_TextView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();
                View loadingIndicator = findViewById(R.id.progressbar_view);
                loadingIndicator.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText("");
                String searchText = searchTextView.getText().toString();
                keywordTextView.setText("Books with title or keyword: " + searchText);
                String adjSearchText = searchText.replace(" ","+");
                String adjGoogleBooksQueryUrl =
                        newGoogleBooksQueryUrl.substring(0,newGoogleBooksQueryUrl.indexOf("?"));

                Uri baseUri = Uri.parse(adjGoogleBooksQueryUrl);
                Uri.Builder uriBuilder = baseUri.buildUpon();
                uriBuilder.appendQueryParameter("q", adjSearchText);
                uriBuilder.appendQueryParameter("maxResults", "20");
                newGoogleBooksQueryUrl = uriBuilder.toString();

                if(isConnected){
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }else{
                    mAdapter.clear();
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }

                //reset the values
                searchTextView.setText("");
                            }
        });

    }



    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, newGoogleBooksQueryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.progressbar_view);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Books, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    }
