package com.example.javafxphotos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MoveAlbumPage extends AppCompatActivity {
    private EditText albumNameEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_photo); // Make sure the layout name matches your XML file

        albumNameEditText = findViewById(R.id.album_name_edittext);
        submitButton = findViewById(R.id.move_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumName = albumNameEditText.getText().toString().trim();
                if (!albumName.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("albumName", albumName);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    albumNameEditText.setError("Album name cannot be empty");
                }
            }
        });
    }
}
