package com.example.javafxphotos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity {

    public static List<Album> albums;
    Button openAlbum;
    public static Album currentAlbum;
    public List<String> albumNames;
    private ListView listView;
    static String selectedValue;
    static File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        albums = new ArrayList<>();
        File filesDir = getFilesDir();
        String fileName = "file.ser";
        file = new File(filesDir, fileName);
        try {
            albums = readAlbumList();
        } catch (IOException | ClassNotFoundException e) {
             System.out.println("e");
        }
        albumNames = getAlbumNames(albums);
        listView = findViewById(R.id.albums_list);
        openAlbum = findViewById(R.id.openAlbum);

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.albums_page, albumNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Time in milliseconds for double click
            long lastClickTime = 0;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long clickTime = System.currentTimeMillis();
                selectedValue = (String) parent.getItemAtPosition(position);
                for(int i = 0; i < albums.size(); i++){
                    if (albums.get(i).getName().equals(selectedValue)){
                        currentAlbum = albums.get(i);
                    }
                }
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    // Double click detected
                    // Open the page here, for example:
                    Intent intent = new Intent(MainActivity.this, PhotosPage.class);
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

        openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedValue != null) {
                    // Start a new activity based on the selected item
                    Intent intent = new Intent(MainActivity.this, EditAlbumsPage.class);
                    try {
                        writeAlbumList(albums);
                        startActivity(intent);
                    } catch (IOException e) {
                        System.out.print("e");
                    }
                }
            }
        });
    }


    public void openAlbum(View view) {
        startActivity(new Intent(this, PhotosPage.class));

    }

    public void addAlbum(View view) {
        startActivity(new Intent(this, AddAlbumPage.class));
    }

    public List<String> getAlbumNames(List<Album> list){
        List<String> listNames = new ArrayList<>();
         for(int i = 0; i < list.size(); i++){
            listNames.add(list.get(i).getName());
        }
         return listNames;
    }


    public static String getName(){
        return selectedValue;
    }

    public static Album getCurrentAlbum(){return currentAlbum;}

    public static List<Album> getAlbums(){
        return albums;
    }




    public List<Album> readAlbumList() throws FileNotFoundException, IOException, ClassNotFoundException{
        List <Album> deserialized = new ArrayList<Album>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            deserialized = (List<Album>) ois.readObject();
            return deserialized;
        }
    }

    public static void writeAlbumList(List <Album> albums) throws FileNotFoundException, IOException{
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(albums);
            oos.close();
        }
    }
}