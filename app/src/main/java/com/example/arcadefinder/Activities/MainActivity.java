package com.example.arcadefinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.arcadefinder.Fragments.GuestFragment;
import com.example.arcadefinder.Fragments.MapFragment;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.MainModel;
import com.example.arcadefinder.Fragments.ProfileFragment;
import com.example.arcadefinder.R;
import com.example.arcadefinder.Fragments.UploadFragment;
import com.example.arcadefinder.ViewModels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        boolean placingMarker = i.getBooleanExtra("placingMarker", false);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getUser().observe(this, new Observer<MainModel>() {
            @Override
            public void onChanged(MainModel mainModel) {
                ParseUser currentUser = mainModel.getUser();
                bottomNavigation = findViewById(R.id.bottomNavigation);
                bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                fragment = new MapFragment();
                                if (placingMarker) {
                                    GameLocation gameLocation = i.getParcelableExtra("gameLocation");
//                                    double[] coordinates = i.getDoubleArrayExtra("coordinates");
//                                    String locationName = i.getStringExtra("locationName");
//                                    String address = i.getStringExtra("address");
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("gameLocation", gameLocation);
//                                    bundle.putDoubleArray("coordinates", coordinates);
//                                    bundle.putString("locationName", locationName);
//                                    bundle.putString("address", address);
                                    fragment.setArguments(bundle);
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
        });
    }
}