package com.example.javafxphotos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;


public class Search extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private String types;
    public static List<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        albums=MainActivity.albums;
        RadioGroup radioGroup = findViewById(R.id.search_type_group);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search_bar);
        Button searchButton = findViewById(R.id.search_button);

        // Setup autocomplete adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(adapter);

        // Handle text input for autocomplete
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateAutocomplete(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Handle search button click
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Placeholder for search functionality
            }
        });
    }

    private void updateAutocomplete(String query) {
        // Simulate an autocomplete function
        List<String> autocomplete=new ArrayList<>();
        for (Album album : albums){
            for(Photo photo : album.getPhotos()){
                for(Tag tag : photo.getTags()){
                    if(tag.value.toLowerCase().startsWith(query.toLowerCase()) && tag.name.toLowerCase().equals(types.toLowerCase())){
                        autocomplete.add(tag.value);
                    }
                }
            }
        }
        String[] items = new String[]{"Example 1", "Example 2", "Example 3"};
        adapter.clear();
        adapter.addAll(items);
        adapter.getFilter().filter(query, null);
    }
}

