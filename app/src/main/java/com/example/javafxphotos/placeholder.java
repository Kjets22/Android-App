package com.example.javafxphotos;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class placeholder extends AppCompatActivity {
    private Photo photo;
    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*findViewById(R.id.addTagsButton).setOnClickListener(v -> {
            //Intent intent = new Intent(placeholder.this, AddTagActivity.class);
            //startActivityForResult(intent, 1);
        });*/
    }

    // Receive the result from AddTagActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String type = data.getStringExtra("tagType");
            String value = data.getStringExtra("tagValue");
            Tag tag=new Tag(type, value);
            photo.add_tag(tag);
            //photo.displayTags();
        }
    }
}

