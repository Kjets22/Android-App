package com.example.javafxphotos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NameAdapter extends ArrayAdapter<Album> {
    private Context mContext;
    private int mResource;

    public NameAdapter(Context context, int resource, List<Album> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        Album item = getItem(position);
        TextView textView = convertView.findViewById(R.id.text1);
        if (item != null) {
            textView.setText(item.getName());
        }

        return convertView;
    }
}
