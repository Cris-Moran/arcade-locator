package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arcadefinder.R;

public class QueryActivity extends AppCompatActivity {

    EditText etGameQuery;
    Spinner spinnerRadii;
    Button btnSubmitQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        etGameQuery = findViewById(R.id.etGameQuery);
        spinnerRadii = findViewById(R.id.spinnerRadii);
        btnSubmitQuery = findViewById(R.id.btnSubmitQuery);

        // https://code.tutsplus.com/tutorials/how-to-add-a-dropdown-menu-in-android-studio--cms-37860
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.radii, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerRadii.setAdapter(adapter);

        btnSubmitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etGameQuery.equals("")) {
                    Toast.makeText(QueryActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(QueryActivity.this, MainActivity.class);
                i.putExtra("gameTitle", etGameQuery.getText().toString());

                // Convert string from drop down into an integer
                String radiusString = spinnerRadii.getSelectedItem().toString();
                int radius = Integer.parseInt(radiusString.split(" ")[0]);

                i.putExtra("radius", radius);
                i.putExtra("querying", true);
                startActivity(i);
            }
        });
    }
}