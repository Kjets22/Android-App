package com.example.javafxphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;

public class DisplayPage extends AppCompatActivity {

    Photo currentPhoto;
    ImageView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);
        pic = findViewById(R.id.curPic);
        currentPhoto = PhotosPage.getCurrentPhoto();
        Uri imageUri = Uri.parse(currentPhoto.getPath());
        Bitmap bitmap;
        findViewById(R.id.addTag).setOnClickListener(v -> {
            Intent intent = new Intent(DisplayPage.this, AddTagPage.class);
            startActivityForResult(intent, 1);
        });
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            pic.setImageBitmap(bitmap);
            getContentResolver().takePersistableUriPermission(imageUri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String type = data.getStringExtra("tagType");
            String value = data.getStringExtra("tagValue");
            Tag tag=new Tag(type, value);
            currentPhoto.add_tag(tag);
            //photo.displayTags();
        }
    }
}