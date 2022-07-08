package com.example.arcadefinder.Models;

import com.example.arcadefinder.GameLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Map;

public class MapModel {

    private List<GameLocation> locationList;
    private List<Marker> markers;
    private String query;
    private boolean locationPermission;
    private double radius;
    private Circle circle;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
