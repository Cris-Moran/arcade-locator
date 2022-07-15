package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.arcadefinder.Adapters.RequestAdapter;
import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.AdminModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.AdminViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    protected List<ParseGameLocation> requests;
    protected RequestAdapter adapter;
    AdminViewModel adminViewModel;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rvRequests = findViewById(R.id.rvRequests);

        // initialize the array that will hold posts and create a PostsAdapter
        requests = new ArrayList<>();
        adapter = new RequestAdapter(this, requests);

        // set the adapter on the recycler view
        rvRequests.setAdapter(adapter);
        // set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRequests.setLayoutManager(linearLayoutManager);
        // add divider in between requests
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRequests.getContext(), linearLayoutManager.getOrientation());
        rvRequests.addItemDecoration(dividerItemDecoration);

        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.getAdminModel().observe(this, new Observer<AdminModel>() {
            @Override
            public void onChanged(AdminModel adminModel) {
                List<ParseGameLocation> locations = adminModel.getLocations();
                if (locations != null) {
                    requests.addAll(locations);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // query requests from Parse
        adminViewModel.queryRequests();
    }



}