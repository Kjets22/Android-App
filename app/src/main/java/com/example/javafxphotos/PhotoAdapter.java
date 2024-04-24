package com.example.javafxphotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PhotoAdapter extends ArrayAdapter<Photo> {

    private LayoutInflater inflater;
    private static Handler handler = new Handler();

    public PhotoAdapter(Context context, List<Photo> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.photos_page, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Photo item = getItem(position);
        holder.textView.setText(item.getLocation());

        // Load image from URL using Thread
        new Thread(new DownloadImageTask(holder.imageView, item.getLocation())).start();

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    private static class DownloadImageTask implements Runnable {
        private final ImageView imageView;
        private final String imageUrl;

        DownloadImageTask(ImageView imageView, String imageUrl) {
            this.imageView = imageView;
            this.imageUrl = imageUrl;
        }

        @Override
        public void run() {
            Bitmap bitmap = downloadBitmap(imageUrl);
            if (bitmap != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }

        private Bitmap downloadBitmap(String imageUrl) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
