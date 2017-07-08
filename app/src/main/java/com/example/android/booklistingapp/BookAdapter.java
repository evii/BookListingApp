package com.example.android.booklistingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

        if (convertView == null) {
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

            // Find the ImageView dedicated to the bookcover
            ImageView coverImageView = (ImageView) listItemView.findViewById(R.id.cover_ImageView);
            String pictureUrl = currentBook.getPictureUrl();

            AsyncTask<ImageView, Void, Bitmap> imageViewVoidBitmapAsyncTask = new DownloadImagesTask(pictureUrl)
                    .execute(coverImageView);

        }
        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }


    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        private ImageView imageView;
        private String url;

        public DownloadImagesTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image(url);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap download_Image(String urlParam) {

            Bitmap bmp = null;
            try {
                URL url = new URL(urlParam);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            } catch (Exception e) {
            }
            return bmp;
        }
    }

}
