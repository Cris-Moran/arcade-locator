package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.Fragments.MapFragment;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.R;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

public class RequestDetailsActivity extends AppCompatActivity {

    GameLocation gameLocation;
    ImageView ivGameImageDetail;
    TextView tvGameTitleDetail;
    TextView tvGameAddressDetail;
    TextView tvGameDescriptionDetail;
    Button btnReject;
    Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        ivGameImageDetail = findViewById(R.id.ivGameImageDetails);
        tvGameTitleDetail = findViewById(R.id.tvGameTitleDetail);
        tvGameAddressDetail = findViewById(R.id.tvGameAddressDetail);
        tvGameDescriptionDetail = findViewById(R.id.tvGameDescriptionDetail);
        btnReject = findViewById(R.id.btnReject);
        btnAccept = findViewById(R.id.btnAccept);

        gameLocation = getIntent().getParcelableExtra("PARSE_OBJECT_EXTRA");
        tvGameTitleDetail.setText(gameLocation.getTitle());
        String addressText = gameLocation.getLocationName() + "\n" + gameLocation.getAddress();
        tvGameAddressDetail.setText(addressText);
        tvGameDescriptionDetail.setText(gameLocation.getDescription());
        ParseFile image = gameLocation.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivGameImageDetail);
        }
        
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RequestDetailsActivity.this, "Request was rejected", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RequestDetailsActivity.this, MainActivity.class);
                ParseGeoPoint coordinates = gameLocation.getCoordinates();
                double lat = coordinates.getLatitude();
                double lng = coordinates.getLongitude();
                i.putExtra("acceptingLocation", true);
//                i.putExtra("coordinates", new double[]{lat, lng});
//                i.putExtra("locationName", gameLocation.getLocationName());
//                i.putExtra("address", gameLocation.getAddress());
                i.putExtra("gameLocation", gameLocation);
                startActivity(i);
                finish();
            }
        });
    }

}