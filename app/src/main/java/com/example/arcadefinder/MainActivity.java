package com.example.arcadefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.arcadefinder.Models.MainModel;
import com.example.arcadefinder.ViewModels.MainViewModel;
import com.example.arcadefinder.ViewModels.RegisterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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