package com.example.javafxphotos;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Album> albums;
    private ListView listView;
    private PopupWindow popupWindow;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        albums = new ArrayList<>();

        File filesDir = getFilesDir();

        // Specify the file name
        String fileName = "file.ser";

        // Construct the file path
        file = new File(filesDir, fileName);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        */
        try {
            albums = readAlbumList();
        } catch (IOException | ClassNotFoundException e) {
             System.out.println("e");
        }

        listView = findViewById(R.id.albums_list);

        NameAdapter adapter = new NameAdapter(this, R.layout.albums_page, albums);
        listView.setAdapter(adapter);


    }

    public void openAlbum(View view) {
        startActivity(new Intent(this, PhotosPage.class));
    }

    public void addAlbum(View view) {
        startActivity(new Intent(this, AddAlbumPage.class));
    }

    private void showPopupWindow() {
        // Inflate the popup_window.xml layout file
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup parentView = findViewById(R.id.main);
        View popupView = inflater.inflate(R.layout.albums_error, parentView);

        // Initialize a new instance of PopupWindow
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Set an animation for the popup window
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        // Set a background drawable with round corners
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Set focusable and outside touchable to dismiss the popup window
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        // Show the popup window at the center of the screen
        popupWindow.showAtLocation(
                findViewById(R.id.main),  // Location to display popup window
                Gravity.CENTER,                   // Exact position of layout to display popup
                0,                               // X offset
                0                                // Y offset
        );
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