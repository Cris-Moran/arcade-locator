package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.MapModel;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapRepo {

    public final String TAG = "MapRepo";

    public void queryLocations(String gameTitle, double radius, ParseGeoPoint currentLocation, MutableLiveData<MapModel> mutableLiveData) {
        // specify what type of data we want to query - Post.class
        ParseQuery<ParseGameLocation> query = ParseQuery.getQuery(ParseGameLocation.class);
        // get queries near a certain radius
        query.whereWithinMiles(ParseGameLocation.KEY_COORDINATES, currentLocation, radius);
        // get only locations that are verified
        query.whereEqualTo(ParseGameLocation.KEY_VERIFIED, true);
        // get locations with the correct title
        query.whereEqualTo(ParseGameLocation.KEY_TITLE, gameTitle);
        // start an asynchronous call for locations
        query.findInBackground(new FindCallback<ParseGameLocation>() {
            @Override
            public void done(List<ParseGameLocation> locations, ParseException e) {
                // check for errors
                if (e != null) {
                    return;
                }
                MapModel mapModel = mutableLiveData.getValue();
                if (locations.isEmpty()) {
                    mapModel.setQueryStatus(false);
                } else {
                    mapModel.setQueryStatus(true);
                    mapModel.setSearchBarQuery(true);
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
        List<ParseGameLocation> parseGameLocations = new ArrayList<>();
        mapModel.setLocationList(parseGameLocations);
        mutableLiveData.setValue(mapModel);
        return mutableLiveData;
    }

    public HashMap<String, Object> getLocationFields(ParseGameLocation parseGameLocation) {
        HashMap<String, Object> locationFields = new HashMap<>();

        ParseGeoPoint coordinates = parseGameLocation.getCoordinates();
        double lat = coordinates.getLatitude();
        double lng = coordinates.getLongitude();
        LatLng markerLocation = new LatLng(lat, lng);
        String gameTitle = parseGameLocation.getTitle();
        String address = parseGameLocation.getLocationName() + "\n" +  parseGameLocation.getAddress();

        locationFields.put("markerLocation", markerLocation);
        locationFields.put("gameTitle", gameTitle);
        locationFields.put("address", address);

        return locationFields;
    }

    public String getLocationId(ParseGameLocation parseGameLocation) {
        return parseGameLocation.getObjectId();
    }

    public void queryLocationById(List<String> ids, MutableLiveData<MapModel> mutableLiveData) {
        // specify what type of data we want to query - Post.class
        ParseQuery<ParseGameLocation> query = ParseQuery.getQuery(ParseGameLocation.class);
        query.whereContainedIn(ParseGameLocation.KEY_ID, ids);
        query.setLimit(20);
        // start an asynchronous call for locations
        query.findInBackground(new FindCallback<ParseGameLocation>() {
            @Override
            public void done(List<ParseGameLocation> objects, ParseException e) {
                // check for errors
                if (e != null) {
                    return;
                }
                MapModel mapModel = mutableLiveData.getValue();
                if (objects.isEmpty()) {
                    mapModel.setQueryStatus(false);
                } else {
                    mapModel.setQueryStatus(true);
                    mapModel.setLocationList(objects);
                }
                mutableLiveData.setValue(mapModel);
            }
        });
    }
}
