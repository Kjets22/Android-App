package com.example.javafxphotos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class DisplayPage extends AppCompatActivity {

    Photo currentPhoto;
    Album currentAlbum;
    ImageView pic;
    ListView listView;
    List<Tag> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);
        pic = findViewById(R.id.curPic);
        listView = findViewById(R.id.tags_list);
        currentPhoto = PhotosPage.getCurrentPhoto();
        currentAlbum= MainActivity.getCurrentAlbum();
        //currentPhoto.add_tag(new Tag("location","my house"));
        tags=currentPhoto.getTags();
        Uri imageUri = Uri.parse(currentPhoto.getPath());
        Bitmap bitmap;
        findViewById(R.id.addTag).setOnClickListener(v -> {
            Intent intent = new Intent(DisplayPage.this, AddTagPage.class);
            startActivityForResult(intent, 1);
        });
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            pic.setImageBitmap(bitmap);
            getContentResolver().takePersistableUriPermission(imageUri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TagAdapter adapter = new TagAdapter(this, tags);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String type = data.getStringExtra("tagType");
            String value = data.getStringExtra("tagValue");
            Tag tag=new Tag(type, value);
            for(Photo photo : currentAlbum.getPhotos()){
                if(currentPhoto.imagePath.equals(photo.imagePath)){
                    photo.add_tag(tag);
                    Toast.makeText(this, "found photo", Toast.LENGTH_SHORT).show();
                    try {
                        MainActivity.writeAlbumList(MainActivity.albums);
                    } catch (IOException e) {
                        System.out.print("e");
                    }
                    currentPhoto.add_tag(tag);
                    break;
                }
            }
            tags=currentPhoto.getTags();
            TagAdapter adapter = new TagAdapter(this, tags);
            listView.setAdapter(adapter);

        }
    }
}

 class TagAdapter extends ArrayAdapter<Tag> {
    public TagAdapter(Context context, List<Tag> tags) {
        super(context, 0, tags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_tag, parent, false);
        }

        // Get the data item for this position
        Tag tag = getItem(position);

        // Lookup view for data population
        TextView tvType = convertView.findViewById(R.id.tvType);
        TextView tvValue = convertView.findViewById(R.id.tvValue);

        // Populate the data into the template view using the data object
        tvType.setText(tag.getName());
        tvValue.setText(tag.getValue());

        // Return the completed view to render on screen
        return convertView;
    }
}