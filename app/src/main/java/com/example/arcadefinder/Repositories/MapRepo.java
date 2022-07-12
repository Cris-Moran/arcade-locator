package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Activities.AdminActivity;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.LoginModel;
import com.example.arcadefinder.Models.MapModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MapRepo {

    public final String TAG = "MapRepo";

    public void queryLocations(String gameTitle, double radius, ParseGeoPoint currentLocation, MutableLiveData<MapModel> mutableLiveData) {
        // specify what type of data we want to query - Post.class
        ParseQuery<GameLocation> query = ParseQuery.getQuery(GameLocation.class);
        // get queries near a certain radius
        query.whereWithinMiles(GameLocation.KEY_COORDINATES, currentLocation, radius);
        // get only locations that are verified
        query.whereEqualTo(GameLocation.KEY_VERIFIED, true);
        // get locations with the correct title
        query.whereEqualTo(GameLocation.KEY_TITLE, gameTitle);
        // limit query to latest 50 items
        query.setLimit(20);
        // start an asynchronous call for locations
        query.findInBackground(new FindCallback<GameLocation>() {
            @Override
            public void done(List<GameLocation> locations, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                MapModel mapModel = mutableLiveData.getValue();
                if (locations.isEmpty()) {
                    mapModel.setQueryStatus(false);
                } else {
                    mapModel.setLocationList(locations);
                    mapModel.setRadius(radius);
                }
                mutableLiveData.setValue(mapModel);
            }
        });
    }


    public MutableLiveData<MapModel> getMapModel() {
        final MutableLiveData<MapModel> mutableLiveData = new MutableLiveData<>();
        MapModel mapModel = new MapModel();
        List<GameLocation> gameLocations = new ArrayList<>();
        mapModel.setLocationList(gameLocations);
        mutableLiveData.setValue(mapModel);
        return mutableLiveData;
    }

    public HashMap<String, Object> getLocationFields(GameLocation gameLocation) {
        HashMap<String, Object> locationFields = new HashMap<>();

        ParseGeoPoint coordinates = gameLocation.getCoordinates();
        double lat = coordinates.getLatitude();
        double lng = coordinates.getLongitude();
        LatLng markerLocation = new LatLng(lat, lng);
        String gameTitle = gameLocation.getTitle();
        String address = gameLocation.getAddress();

        locationFields.put("markerLocation", markerLocation);
        locationFields.put("gameTitle", gameTitle);
        locationFields.put("address", address);

        return locationFields;
    }
}
