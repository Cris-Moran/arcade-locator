package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.arcadefinder.Adapters.RequestAdapter;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    protected List<GameLocation> requests;
    protected RequestAdapter adapter;

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
        // query requests from Parse
        queryRequests();
    }

    private void queryRequests() {
        // specify what type of data we want to query - Post.class
        ParseQuery<GameLocation> query = ParseQuery.getQuery(GameLocation.class);
        // include data referred by user key
        query.include(GameLocation.KEY_AUTHOR);
        // for endless scrolling: if we are extending the feed than skip the first 20
//        if (extendingFeed) {
//            offset += 20;
//            query.setSkip(offset);
//        } else {
//            query.setSkip(0);
//        }
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<GameLocation>() {
            @Override
            public void done(List<GameLocation> requests, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // save received posts to list and notify adapter of new data
                AdminActivity.this.requests.addAll(requests);
                adapter.notifyDataSetChanged();
            }
        });
    }

}