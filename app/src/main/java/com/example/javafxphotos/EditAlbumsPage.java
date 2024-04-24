package com.example.javafxphotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

public class EditAlbumsPage extends AppCompatActivity {
    public List<Album> albums;
    Album currentAlbum;
    private EditText albumName;
    private TextView t;

    // Get a reference to the files directory


    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_albums_page);
        albums = new ArrayList<>();
        albumName = findViewById(R.id.albumName2);
        currentAlbum = MainActivity.getCurrentAlbum();
        t = findViewById(R.id.textView2);
        albumName.setText(MainActivity.getName());
        File filesDir = getFilesDir();
        String fileName = "file.ser";

        // Construct the file path
        file = new File(filesDir, fileName);
        try {
            albums = readAlbumList();
        } catch (IOException | ClassNotFoundException e) {
            System.out.print("e");
        }
    }

    public void deleteAlbum(View view) throws IOException {
        for(int i = 0; i < albums.size(); i++){
            if(albums.get(i).getName().equals(currentAlbum.getName())){
                albums.remove(i);
                writeAlbumList(albums);
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    public void save(View view) throws IOException {
        for(int i = 0; i < albums.size(); i++){
            if(albums.get(i).getName().equals(currentAlbum.getName())){
                albums.get(i).set_name(albumName.getText().toString());
                writeAlbumList(albums);
                startActivity(new Intent(this, MainActivity.class));
            }
        }
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