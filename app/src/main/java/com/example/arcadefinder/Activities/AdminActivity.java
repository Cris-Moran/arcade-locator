package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import com.example.arcadefinder.Adapters.GameLocationAdapter;
import com.example.arcadefinder.Models.GameLocationModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.AdminViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    protected List<GameLocationModel> locationRequests;
    protected GameLocationAdapter adapter;
    AdminViewModel adminViewModel;
    boolean isConnectedToNetwork;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rvRequests = findViewById(R.id.rvRequests);
        locationRequests = new ArrayList<>();
        adapter = new GameLocationAdapter(this, locationRequests);

        // set the adapter on the recycler view
        rvRequests.setAdapter(adapter);

        // set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRequests.setLayoutManager(linearLayoutManager);
        // add divider in between requests
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRequests.getContext(), linearLayoutManager.getOrientation());
        rvRequests.addItemDecoration(dividerItemDecoration);

        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.getGameLocations().observe(this, new Observer<List<GameLocationModel>>() {
            @Override
            public void onChanged(List<GameLocationModel> gameLocationModels) {
                if (!gameLocationModels.isEmpty()) {
                    locationRequests.addAll(gameLocationModels);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        isConnectedToNetwork = isNetworkAvailable();

        if (isConnectedToNetwork) {
            adminViewModel.queryRequests();
        } else {
            adminViewModel.getRequestsOffline();
        }

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
