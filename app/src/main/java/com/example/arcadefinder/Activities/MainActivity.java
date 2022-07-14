package com.example.arcadefinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.arcadefinder.Fragments.GuestFragment;
import com.example.arcadefinder.Fragments.MapFragment;
import com.example.arcadefinder.Fragments.NotFoundDialogFragment;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.MainModel;
import com.example.arcadefinder.Fragments.ProfileFragment;
import com.example.arcadefinder.R;
import com.example.arcadefinder.Fragments.UploadFragment;
import com.example.arcadefinder.ViewModels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NotFoundDialogFragment.NotFoundDialogListener {

    final String TAG = getClass().getSimpleName();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;
    private MainViewModel mainViewModel;
    private ParseUser currentUser;

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
                boolean verifyStatus = mainModel.isVerifyStatus();
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
                            i.removeExtra("acceptingLocation");
                        }
                        if (querying) {
                            String gameTitle = i.getStringExtra("gameTitle");
                            setUpQuery(fragment, gameTitle);
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

    @Override
    public void onItemSelected(String selectedItem) {
        Fragment fragment = new MapFragment();
        setUpQuery(fragment, selectedItem);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    public void setUpQuery(Fragment fragment, String query) {
        // Pass information from search query into map fragment
        Intent i = getIntent();

        int radius = i.getIntExtra("radius", 5);
        ArrayList<String> suggestions = i.getStringArrayListExtra("suggestions");

        Bundle bundle = new Bundle();

        bundle.putString("gameTitle", query);
        bundle.putInt("radius", radius);
        bundle.putStringArrayList("suggestions", suggestions);
        fragment.setArguments(bundle);
        i.removeExtra("querying");
    }
}