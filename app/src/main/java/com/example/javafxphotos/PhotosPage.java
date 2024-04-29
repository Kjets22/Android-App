package com.example.javafxphotos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotosPage extends AppCompatActivity {
    public List<Album> albums;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 200;
    public List<Photo> photos;
    Album currentAlbum;
    static Photo currentPhoto;
    Button addImage;
    Button d;
    private ListView listView;
    static Photo selectedValue;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_page);
        File filesDir = getFilesDir();
        String fileName = "file.ser";
        file = new File(filesDir, fileName);
        addImage = findViewById(R.id.addPic);
        addImage.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                requestStoragePermission();
            }});
        albums = new ArrayList<>();
        try {
            albums = readAlbumList();
        } catch (IOException | ClassNotFoundException e) {
            System.out.print("e");
        }
        currentAlbum = MainActivity.getCurrentAlbum();
        photos = getPhotosList();

        listView = findViewById(R.id.photosList);
        d = findViewById(R.id.delete_B);

        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Time in milliseconds for double click
            long lastClickTime = 0;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long clickTime = System.currentTimeMillis();
                selectedValue = (Photo) parent.getItemAtPosition(position);
                for(int i = 0; i < photos.size(); i++){
                    if (photos.get(i) == selectedValue){
                        currentPhoto = photos.get(i);
                    }
                }//maybe the error is here mabye come back and check
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    // Double click detected
                    // Open the page here, for example:
                    Intent intent = new Intent(PhotosPage.this, DisplayPage.class);
                    try {
                        writeAlbumList(albums);
                    } catch (IOException e) {
                        System.out.print("e");
                    }
                    startActivity(intent);
                }
                lastClickTime = clickTime;
            }


        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePhoto(v);
            }
        });


    }

    public static Photo getCurrentPhoto(){
        return currentPhoto;
    }

    public static Photo setCurrentPhoto(Photo newP){
        currentPhoto = newP;
        return currentPhoto;
    }


    private void requestStoragePermission() {
        // Log to diagnose
        Log.d("Permissions", "Requesting storage permission.");

        // Check if permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permissions", "Permission not granted. Requesting permission.");

            // No explanation needed, just request the permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_MEDIA_IMAGES},REQUEST_STORAGE_PERMISSION);
            openGallery();
        } else {
            // Permission has already been granted
            Log.d("Permissions", "Permission already granted. Proceeding to open gallery.");
            openGallery();
        }
    }

    private void deletePhoto(View view){
        if (selectedValue != null) {
            // Start a new activity based on the selected item
            for(int i = 0; i < albums.size(); i++){
                if (albums.get(i).getName().equals(currentAlbum.getName())){
                    for(int j = 0; j < albums.get(i).getPhotos().size(); j++){
                        if(albums.get(i).getPhotos().get(j) == selectedValue){
                            albums.get(i).getPhotos().remove(j);
                            try {
                                writeAlbumList(albums);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Intent intent = new Intent(PhotosPage.this, PhotosPage.class);
                            startActivity(intent);

                        }
                    }
                }
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri targetUri = data.getData();
            String path = targetUri.toString();
            for(int i = 0; i < albums.size(); i++) {
                if(albums.get(i).getName().equals(currentAlbum.getName())){
                    albums.get(i).add_photo(path);}
            }
            try {
                writeAlbumList(albums);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            photos = getPhotosList();
            PhotoAdapter adapter = new PhotoAdapter(this, photos);
            listView.setAdapter(adapter);
        }
    }



    public List<Photo> getPhotosList(){
        List<Photo> pics = new ArrayList<>();
        for(int i = 0; i < albums.size(); i++){
            if(albums.get(i).getName().equals(currentAlbum.getName())){
                for(int j = 0; j < albums.get(i).getPhotos().size(); j++){
                    pics.add(albums.get(i).getPhotos().get(j));
                }
            }
        }
        return pics;
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