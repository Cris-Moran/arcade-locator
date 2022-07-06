package com.example.arcadefinder.Models;

import com.example.arcadefinder.GameLocation;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import java.util.Map;

public class MapModel {

    private List<GameLocation> locationList;
    private boolean locationPermission;


    public boolean getLocationPermission() {
        return locationPermission;
    }

    public void setLocationPermission(boolean locationPermission) {
        this.locationPermission = locationPermission;
    }

    public List<GameLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<GameLocation> locationList) {
        this.locationList = locationList;
    }
}
