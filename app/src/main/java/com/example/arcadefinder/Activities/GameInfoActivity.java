package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.arcadefinder.R;

public class GameInfoActivity extends AppCompatActivity {

    TextView tvMapGameTitle;
    TextView tvMapGameAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String address = i.getStringExtra("address");

        tvMapGameTitle = findViewById(R.id.tvMapGameTitle);
        tvMapGameAddress = findViewById(R.id.tvMapGameAddress);

        tvMapGameTitle.setText(title);
        tvMapGameAddress.setText(address);
    }
}