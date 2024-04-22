package com.example.javafxphotos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AddAlbumPage extends AppCompatActivity {

    public List<Album> albums;

    private EditText albumName;
    // Get a reference to the files directory


    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_album_page);
        albums = new ArrayList<>();
        albumName = findViewById(R.id.albumName);
        File filesDir = getFilesDir();

        // Specify the file name
        String fileName = "file.ser";

        // Construct the file path
        file = new File(filesDir, fileName);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        try {
            albums = readAlbumList();
        } catch (IOException | ClassNotFoundException e) {
            System.out.print("e");
        }
    }

    public void save(View view) throws IOException {
        String name = albumName.getText().toString();
        Album a = new Album(name);
        albums.add(a);
        writeAlbumList(albums);
        startActivity(new Intent(this, PhotosPage.class));
    }

    @SuppressWarnings("unchecked")
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