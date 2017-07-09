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

    // View lookup cache
    private static class ViewHolder {
        TextView authorView;
        TextView titleView;
        ImageView coverImageView;
    }
    public BookAdapter(Activity context, ArrayList<Books> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Find the book at the given position in the list of books
        Books currentBook = getItem(position);

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            // Find the TextView with view ID author
            viewHolder.authorView = (TextView) convertView.findViewById(R.id.author_view);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.title_view);
            viewHolder.coverImageView = (ImageView) convertView.findViewById(R.id.cover_ImageView);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag(); }

            // Display the author of the current book in that TextView
            String author = currentBook.getAuthor();
            viewHolder.authorView.setText(author);

                        // Display the title of the current book in that TextView
            String title = currentBook.getTitle();
            viewHolder.titleView.setText(title);

            // Find the ImageView dedicated to the bookcover

            String pictureUrl = currentBook.getPictureUrl();

            AsyncTask<ImageView, Void, Bitmap> imageViewVoidBitmapAsyncTask = new DownloadImagesTask(pictureUrl)
                    .execute(viewHolder.coverImageView);


        // Return the list item view that is now showing the appropriate data
        return convertView;
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
