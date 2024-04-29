package com.example.javafxphotos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;


public class Search extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private String types="";
    public static List<Album> albums;
    private String search;
    public static ArrayList<Photo> display;

    RadioGroup radioGroup;

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
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton selectedRadioButton = (RadioButton) group.findViewById(checkedId);
                        types = selectedRadioButton.getText().toString();
                        // Use selectedText for further processing
                    }
                });
                //types = selectedRadioButton.getText().toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(types.isEmpty()){
                    Toast.makeText(Search.this, "you need to fill in type first", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                if(types.isEmpty()){
                    Toast.makeText(Search.this, "you need to fill in type first", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean found=false;

                //display.clear();

                display=new ArrayList<>();
                for (Album album : albums){
                    for(Photo photo : album.getPhotos()){
                        found=false;
                        for(Tag tag : photo.getTags()){
                            if(tag.value.equalsIgnoreCase(search) && tag.name.equalsIgnoreCase(types)){
                                found=true;
                                break;
                            }
                        }
                        if(found){
                            display.add(photo);
                        }
                    }
                }
                Toast.makeText(Search.this, "completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Search.this, ResultsPage.class);
                startActivity(intent);
            }
        });
    }

    private void updateAutocomplete(String query) {
        search=query;
        // Simulate an autocomplete function
        List<String> autocomplete=new ArrayList<>();
        for (Album album : albums){
            for(Photo photo : album.getPhotos()){
                for(Tag tag : photo.getTags()){
                    if(tag.value.toLowerCase().startsWith(query.toLowerCase()) && tag.name.equalsIgnoreCase(types)){
                        autocomplete.add(tag.value);
                    }
                }
            }
        }
        List<String> cleaned=new ArrayList<>();
        boolean in=false;
        for(int i=0;i<autocomplete.size();i++){
            in=false;
            for(int j=0;j<cleaned.size();j++){
                if(cleaned.get(j).equalsIgnoreCase(autocomplete.get(i))){
                    in=true;
                }
            }
            if(!in){
                cleaned.add(autocomplete.get(i));
            }
        }
        adapter.clear();
        if (!cleaned.isEmpty()) {
            adapter.addAll(cleaned);
        } else {
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }
}

