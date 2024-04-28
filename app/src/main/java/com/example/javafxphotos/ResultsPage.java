package com.example.javafxphotos;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResultsPage extends AppCompatActivity {

    ListView photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
        //Toast.makeText(this, "got into new display", Toast.LENGTH_SHORT).show();

        photoList = findViewById(R.id.photosList);

        // Get the list of photos from the intent
        List<Photo> photos = Search.display;

        Toast.makeText(this, "got into new display", Toast.LENGTH_SHORT).show();
        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        photoList.setAdapter(adapter);
    }
}

