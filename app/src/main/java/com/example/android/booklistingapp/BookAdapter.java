package com.example.android.booklistingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by evi on 22. 6. 2017.
 */

public class BookAdapter extends ArrayAdapter<Books> {

    public BookAdapter(Activity context, ArrayList<Books> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Find the book at the given position in the list of books
        Books currentBook = getItem(position);

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.authorView.setText(currentBook.getAuthor());
        viewHolder.titleView.setText(currentBook.getTitle());

        AsyncTask<ImageView, Void, Bitmap> imageViewVoidBitmapAsyncTask = new DownloadImagesTask(currentBook.getPictureUrl())
                .execute(viewHolder.coverImageView);

        // Return the list item view that is now showing the appropriate data
        return convertView;
    }

    // AsyncTask for downaloda of images
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

    // Viewholder implementation
    class ViewHolder {
        private TextView authorView;
        private TextView titleView;
        private ImageView coverImageView;

        public ViewHolder(@NonNull View view) {
            this.authorView = (TextView) view
                    .findViewById(R.id.author_view);
            this.titleView = (TextView) view
                    .findViewById(R.id.title_view);
            this.coverImageView = (ImageView) view
                    .findViewById(R.id.cover_ImageView);
        }
    }
}
