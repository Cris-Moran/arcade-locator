package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.Models.GameLocationModel;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.RequestDetailsViewModel;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class RequestDetailsActivity extends AppCompatActivity {

    GameLocationModel gameLocationModel;
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

        gameLocationModel = Parcels.unwrap(getIntent().getParcelableExtra(GameLocationModel.class.getSimpleName()));
        requestDetailsViewModel = new ViewModelProvider(this).get(RequestDetailsViewModel.class);
        requestDetailsViewModel.getRequestDetailsModel().observe(this, new Observer<RequestDetailsModel>() {
            @Override
            public void onChanged(RequestDetailsModel requestDetailsModel) {
                boolean deleted = requestDetailsModel.isDeleted();
                boolean verified = requestDetailsModel.isVerified();
                if (deleted) {
                    Toast.makeText(RequestDetailsActivity.this, "Request was deleted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RequestDetailsActivity.this, AdminActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
                if (verified) {
                    Toast.makeText(RequestDetailsActivity.this, "Request was verified", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RequestDetailsActivity.this, AdminActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        String title = gameLocationModel.getTitle();
        String address = gameLocationModel.getLocationName() + "\n" + gameLocationModel.getAddress();
        String description = gameLocationModel.getDescription();
        Bitmap image = gameLocationModel.getImage();

        tvGameTitleDetail.setText(title);
        tvGameAddressDetail.setText(address);
        tvGameDescriptionDetail.setText(description);
        ivGameImageDetail.setImageBitmap(image);

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDetailsViewModel.setDeleted(false);
                if (isNetworkAvailable()) {
                    requestDetailsViewModel.deleteLocationOnline(gameLocationModel);
                } else {
                    Toast.makeText(RequestDetailsActivity.this, "Cannot delete a location while offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDetailsViewModel.setVerified(false);
                if (isNetworkAvailable()) {
                    requestDetailsViewModel.verifyLocation(gameLocationModel);
                } else {
                    Toast.makeText(RequestDetailsActivity.this, "Cannot verify a location while offline", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressWarnings("MissingPermission")
    // https://stackoverflow.com/a/57284789
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

}