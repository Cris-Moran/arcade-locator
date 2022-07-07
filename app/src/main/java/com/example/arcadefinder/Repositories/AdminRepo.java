package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Activities.AdminActivity;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.AdminModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class AdminRepo {

    final String TAG = getClass().getSimpleName();

    public void queryRequests(MutableLiveData<AdminModel> mutableLiveData) {
        // specify what type of data we want to query - Post.class
        ParseQuery<GameLocation> query = ParseQuery.getQuery(GameLocation.class);
        // include data referred by user key
        query.include(GameLocation.KEY_AUTHOR);
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
                AdminModel adminModel = mutableLiveData.getValue();
                adminModel.setLocations(requests);
                mutableLiveData.setValue(adminModel);
            }
        });
    }

    public MutableLiveData<AdminModel> getAdminModel() {
        MutableLiveData<AdminModel> mutableLiveData = new MutableLiveData<>();
        AdminModel adminModel = new AdminModel();
        mutableLiveData.setValue(adminModel);
        return mutableLiveData;
    }
}
