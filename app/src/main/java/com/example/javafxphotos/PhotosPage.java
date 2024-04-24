package com.example.javafxphotos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    public List<Photo> photos;
    Album currentAlbum;
    TextView selectedAlbum;
    private ListView listView;

    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photos_page);
        albums = new ArrayList<>();
        currentAlbum = MainActivity.getCurrentAlbum();
        photos = getPhotosList();
        File filesDir = getFilesDir();
        String fileName = "file.ser";
        file = new File(filesDir, fileName);

        selectedAlbum = findViewById(R.id.selectedAlbum);
        selectedAlbum.setText("Album: " + currentAlbum.getName());

        try {
            albums = readAlbumList();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("e");
        }

        listView = findViewById(R.id.photosList);

        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        listView.setAdapter(adapter);

    }

    public List<Photo> getPhotosList(){
        List<Photo> pics = new ArrayList<>();
        for(int i = 0; i < currentAlbum.getPhotos().size(); i++){
            pics.add(currentAlbum.getPhotos().get(i));
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

    public void writeAlbumList(List <Album> albums) throws FileNotFoundException, IOException{
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(albums);
            oos.close();
        }
    }


}