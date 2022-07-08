package com.example.arcadefinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.arcadefinder.Fragments.GuestFragment;
import com.example.arcadefinder.Fragments.MapFragment;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.MainModel;
import com.example.arcadefinder.Fragments.ProfileFragment;
import com.example.arcadefinder.R;
import com.example.arcadefinder.Fragments.UploadFragment;
import com.example.arcadefinder.ViewModels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;
    private MainViewModel mainViewModel;
    private ParseUser currentUser;
    private boolean verifyStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();


        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getUser().observe(this, new Observer<MainModel>() {
            @Override
            public void onChanged(MainModel mainModel) {
                currentUser = mainModel.getUser();
                verifyStatus = mainModel.getVerifyStatus();
                if (verifyStatus) {
                    Toast.makeText(MainActivity.this, "Request was verified!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean acceptingLocation = i.getBooleanExtra("acceptingLocation", false);
                boolean querying = i.getBooleanExtra("querying", false);
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_map:
                        fragment = new MapFragment();
                        if (acceptingLocation) {
                            GameLocation gameLocation = i.getParcelableExtra("gameLocation");
                            mainViewModel.verifyLocation(gameLocation);
                        }
                        if (querying) {
                            // Pass information from search query into map fragment
                            String gameTitle = i.getStringExtra("gameTitle");
                            int radius = i.getIntExtra("radius", 5);

                            Bundle bundle = new Bundle();

                            bundle.putString("gameTitle", gameTitle);
                            bundle.putInt("radius", radius);
                            fragment.setArguments(bundle);
                            i.removeExtra("querying");
                        }
                        break;
                    case R.id.action_upload:
                        if (currentUser != null) {
                            fragment = new UploadFragment();
                        } else {
                            fragment = new GuestFragment();
                        }
                        break;
                    case R.id.action_profile:
                    default:
                        if (currentUser != null) {
                            fragment = new ProfileFragment();
                        } else {
                            fragment = new GuestFragment();
                        }
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.action_map);
    }
}