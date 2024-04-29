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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DisplayPage extends AppCompatActivity {
    List<Album> albums;
    Photo currentPhoto;
    Album currentAlbum;
    ImageView pic;
    ListView listView;
    List<Tag> tags;
    private static final int REQUEST_ADD_TAG = 1;
    private static final int REQUEST_DELETE_TAG = 2;
<<<<<<< HEAD
    private static final int REQUEST_MOVE_PHOTO = 3;
=======
    Bitmap bitmap;
    File file;
    Button l;
    Button r;
>>>>>>> e07a460d91e817545622bee16922d89ff025806c

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);
        File filesDir = getFilesDir();
        String fileName = "file.ser";
        file = new File(filesDir, fileName);
        albums = new ArrayList<>();
        try {
            albums = readAlbumList();
        } catch (IOException | ClassNotFoundException e) {
            System.out.print("e");
        }
        l = findViewById(R.id.left);
        l.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            moveLeft(view);
        }
    });
        r = findViewById(R.id.right);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    moveRight(view);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pic = findViewById(R.id.curPic);
        listView = findViewById(R.id.tags_list);
        currentPhoto = PhotosPage.getCurrentPhoto();
        currentAlbum = MainActivity.getCurrentAlbum();
        //currentPhoto.add_tag(new Tag("location","my house"));
        tags=currentPhoto.getTags();
        Uri imageUri = Uri.parse(currentPhoto.getPath());
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

<<<<<<< HEAD


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
=======
    public void moveLeft(View view){
        for(int i = 0; i < albums.size(); i++){
            if(albums.get(i).getName().equals(currentAlbum.getName())){
                for(int j = 0; j < albums.get(i).getPhotos().size(); j++){
                    if((albums.get(i).getPhotos().get(j).getPath().equals(currentPhoto.getPath()))) {
                        if (j > 0) {
                            Photo pic = albums.get(i).getPhotos().get(j - 1);
                            PhotosPage.setCurrentPhoto(pic);
                            try {
                                writeAlbumList(albums);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Intent intent = new Intent(DisplayPage.this, DisplayPage.class);
                            startActivity(intent);
                        } else {
                            Toast error = Toast.makeText(DisplayPage.this, "Error, you have reached the beginning of the slideshow", Toast.LENGTH_SHORT);
                            error.show();
                        }
                        return;
                    }
                }
            }
        }
    }

    public void moveRight(View view) throws IOException {
        for(int i = 0; i < albums.size(); i++){
            if(albums.get(i).getName().equals(currentAlbum.getName())){
                for(int j = 0; j < albums.get(i).getPhotos().size(); j++){
                    if((albums.get(i).getPhotos().get(j).getPath().equals(currentPhoto.getPath()))){
                        if(j+1 < albums.get(i).getPhotos().size()){
                        Photo pic = albums.get(i).getPhotos().get(j+1);
                        PhotosPage.setCurrentPhoto(pic);
                        try {
                            writeAlbumList(albums);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(DisplayPage.this, DisplayPage.class);
                        startActivity(intent);
                        }
                    else{
                        Toast error = Toast.makeText(DisplayPage.this, "Error, you have reached the end of the slideshow", Toast.LENGTH_SHORT);
                        error.show();
                        }
                        return;
                    }
                }
            }
        }
    }

>>>>>>> e07a460d91e817545622bee16922d89ff025806c
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


    public List<Album> readAlbumList() throws FileNotFoundException, IOException, ClassNotFoundException{
        List <Album> deserialized = new ArrayList<Album>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            deserialized = (List<Album>) ois.readObject();
            return deserialized;
        }
    }

    public void writeAlbumList(List<Album> albums) throws FileNotFoundException, IOException{
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this.albums);
            oos.close();
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