package com.example.javafxphotos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<Photo> images;

    public PhotoAdapter(Context context, List<Photo> imageUris) {
        mContext = context;
        images = imageUris;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.photos_page, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Load image into ImageView using URI
        Uri imageUri = Uri.parse(images.get(position).getPath());
        viewHolder.textView.setText(images.get(position).getPath());
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imageUri));
            viewHolder.imageView.setImageBitmap(bitmap);
            mContext.getContentResolver().takePersistableUriPermission(imageUri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
