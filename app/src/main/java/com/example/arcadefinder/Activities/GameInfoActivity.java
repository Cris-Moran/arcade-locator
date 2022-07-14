package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.GameInfoModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.GameInfoViewModel;
import com.parse.ParseFile;

public class GameInfoActivity extends AppCompatActivity {

    TextView tvMapGameTitle;
    ImageView ivMapGameImage;
    TextView tvMapGameAddress;
    TextView tvMapGameUserDesc;
    TextView tvGameWiki;
    GameInfoViewModel gameInfoViewModel;

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

        gameInfoViewModel = new ViewModelProvider(this).get(GameInfoViewModel.class);
        gameInfoViewModel.getGameInfoModel(gameLocation).observe(this, new Observer<GameInfoModel>() {
            @Override
            public void onChanged(GameInfoModel gameInfoModel) {
                String title = gameInfoModel.getTitle();
                ParseFile image = gameInfoModel.getImage();
                String address = gameInfoModel.getAddress();
                String userDesc = gameInfoModel.getUserDesc();
                String wikiUrl = "More Info:\nhttp://en.wikipedia.org/wiki/" + gameInfoModel.getEncodeTitle();

                tvMapGameTitle.setText(title);
                Glide.with(GameInfoActivity.this).load(image.getUrl()).into(ivMapGameImage);
                tvMapGameAddress.setText(address);
                tvMapGameUserDesc.setText(userDesc);
                tvGameWiki.setText(wikiUrl);
            }
        });

    }
}