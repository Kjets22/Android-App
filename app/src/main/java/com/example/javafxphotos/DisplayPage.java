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
    private static final int REQUEST_ADD_TAG = 1;
    private static final int REQUEST_DELETE_TAG = 2;
    private static final int REQUEST_MOVE_PHOTO = 3;

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
            startActivityForResult(intent, REQUEST_ADD_TAG);
        });

        findViewById(R.id.deleteTag).setOnClickListener(v -> {
            Intent intent = new Intent(DisplayPage.this, AddTagPage.class);
            startActivityForResult(intent, REQUEST_DELETE_TAG);
        });
        findViewById(R.id.moveto).setOnClickListener(v-> {
            Intent intent = new Intent(DisplayPage.this, MoveAlbumPage.class);
            startActivityForResult(intent, REQUEST_MOVE_PHOTO);
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
        if (resultCode == RESULT_OK) {
            String type;
            String value;
            Tag tag;
            switch (requestCode) {
                case REQUEST_ADD_TAG:
                     type = data.getStringExtra("tagType");
                     value = data.getStringExtra("tagValue");
                     tag = new Tag(type, value);
                    addTag(tag);
                    break;
                case REQUEST_DELETE_TAG:
                    type = data.getStringExtra("tagType");
                    value = data.getStringExtra("tagValue");
                    tag = new Tag(type, value);
                    addTag(tag);
                    deleteTag(tag);
                    break;
                case REQUEST_MOVE_PHOTO:
                    String targetAlbumName = data.getStringExtra("albumName");
                    movePhotoToAlbum(targetAlbumName);

                    break;
            }
        }
    }



    private void movePhotoToAlbum(String target) {
        boolean found=false;
        Photo findphoto;
        for (Photo photo : currentAlbum.getPhotos()) {
            if (currentPhoto.imagePath.equals(photo.imagePath)) {
                findphoto=photo;
                currentAlbum.getPhotos().remove(photo);
                for(Album album: MainActivity.getAlbums()){
                    if(album.name.equals(target)) {

                        album.add_photo(findphoto);
                        Toast.makeText(this, "moved", Toast.LENGTH_SHORT).show();
                        try {
                            MainActivity.writeAlbumList(MainActivity.albums);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        found=true;
                        Intent mainActivityIntent = new Intent(DisplayPage.this, MainActivity.class);

                        startActivity(mainActivityIntent);
                        finish();
                        break;
                    }
                }
                if (!found){
                Toast.makeText(this, "name not found", Toast.LENGTH_SHORT).show();}
            }
        }
        refreshListView();

    }
    private void refreshListView() {
        TagAdapter adapter = new TagAdapter(this, tags);
        listView.setAdapter(adapter);
    }
    private void addTag(Tag tag) {
        for (Photo photo : currentAlbum.getPhotos()) {
            if (currentPhoto.imagePath.equals(photo.imagePath)) {
                photo.add_tag(tag);
                Toast.makeText(this, "Tag added", Toast.LENGTH_SHORT).show();
                try {
                    MainActivity.writeAlbumList(MainActivity.albums);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentPhoto.add_tag(tag);
                break;
            }
        }
        refreshListView();
    }

    private void deleteTag(Tag tag) {
        for (Photo photo : currentAlbum.getPhotos()) {
            if (currentPhoto.imagePath.equals(photo.imagePath)) {
                int size=photo.getTags().size();
                photo.remove_tag(tag); // Assuming there's a method to remove a tag
                currentPhoto.remove_tag(tag);
                int newsize=photo.getTags().size();
                if(size==newsize){
                    Toast.makeText(this, "Tag was not found", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Tag deleted", Toast.LENGTH_SHORT).show();
                }
                try {
                    MainActivity.writeAlbumList(MainActivity.albums);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        tags = currentPhoto.getTags();
        refreshListView();
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