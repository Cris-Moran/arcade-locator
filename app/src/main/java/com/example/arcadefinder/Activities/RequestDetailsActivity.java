package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.RequestDetailsViewModel;
import com.parse.ParseFile;

public class RequestDetailsActivity extends AppCompatActivity {

    ParseGameLocation parseGameLocation;
    ImageView ivGameImageDetail;
    TextView tvGameTitleDetail;
    TextView tvGameAddressDetail;
    TextView tvGameDescriptionDetail;
    Button btnReject;
    Button btnAccept;
    RequestDetailsViewModel requestDetailsViewModel;

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

        requestDetailsViewModel = new ViewModelProvider(this).get(RequestDetailsViewModel.class);

        parseGameLocation = getIntent().getParcelableExtra("PARSE_OBJECT_EXTRA");
        requestDetailsViewModel.getRequestDetailsModel(parseGameLocation).observe(this, new Observer<RequestDetailsModel>() {
            @Override
            public void onChanged(RequestDetailsModel requestDetailsModel) {
                String title = requestDetailsModel.getTitle();
                String address = requestDetailsModel.getAddressText();
                String description = requestDetailsModel.getDescription();
                ParseFile image = requestDetailsModel.getImage();
                boolean deleted = requestDetailsModel.isDeleted();

                tvGameTitleDetail.setText(title);
                tvGameAddressDetail.setText(address);
                tvGameDescriptionDetail.setText(description);
                if (image != null) {
                    Glide.with(RequestDetailsActivity.this).load(image.getUrl()).into(ivGameImageDetail);
                }
                if (deleted) {
                    Toast.makeText(RequestDetailsActivity.this, "Request was rejected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDetailsViewModel.deleteLocation(parseGameLocation);
                Intent i = new Intent(RequestDetailsActivity.this, AdminActivity.class);
                startActivity(i);
            }
        });

        // TODO: Maybe you don't have to go back to the map..
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RequestDetailsActivity.this, MainActivity.class);
                i.putExtra("acceptingLocation", true);
                i.putExtra("parseGameLocation", parseGameLocation);
                startActivity(i);
                finish();
            }
        });
    }

}