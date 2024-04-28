package com.example.javafxphotos;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class AddTagPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        EditText tagValue = findViewById(R.id.tag_value_edittext);
        RadioGroup tagTypeGroup = findViewById(R.id.tag_type_group);
        Button submitButton = findViewById(R.id.submit_tag_button);

        submitButton.setOnClickListener(v -> {
            int selectedId = tagTypeGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId);
            String tagType = selectedRadioButton.getText().toString();
            String value = tagValue.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("tagType", tagType);
            resultIntent.putExtra("tagValue", value);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}
