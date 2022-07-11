package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.R;
import com.parse.ParseFile;

public class GameInfoActivity extends AppCompatActivity {

    TextView tvMapGameTitle;
    ImageView ivMapGameImage;
    TextView tvMapGameAddress;
    TextView tvMapGameUserDesc;
    TextView tvGameWiki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        Intent i = getIntent();
        GameLocation gameLocation = i.getParcelableExtra("gameLocation");

        tvMapGameTitle = findViewById(R.id.tvMapGameTitle);
        ivMapGameImage = findViewById(R.id.ivMapGameImage);
        tvMapGameAddress = findViewById(R.id.tvMapGameAddress);
        tvMapGameUserDesc = findViewById(R.id.tvMapGameUserDesc);
        tvGameWiki = findViewById(R.id.tvGameWiki);

        String title = gameLocation.getTitle();
        ParseFile image = gameLocation.getImage();
        String address = gameLocation.getAddress();
        String userDesc = gameLocation.getDescription();
        tvMapGameTitle.setText(title);
        Glide.with(this).load(image.getUrl()).into(ivMapGameImage);
        tvMapGameAddress.setText(address);
        tvMapGameUserDesc.setText(userDesc);
    }
}