package com.example.arcadefinder.Models;

import com.example.arcadefinder.ParseGameLocation;

import java.util.List;

public class MapModel {

    private List<ParseGameLocation> locationList;
    private String query;
    private boolean locationPermission;
    private double radius;
    private boolean searchBarQuery;
    private Boolean queryStatus;

    public Boolean getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(Boolean queryStatus) {
        this.queryStatus = queryStatus;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isLocationPermission() {
        return locationPermission;
    }

    public void setLocationPermission(boolean locationPermission) {
        this.locationPermission = locationPermission;
    }

    public List<ParseGameLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<ParseGameLocation> locationList) {
        this.locationList = locationList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isSearchBarQuery() {
        return searchBarQuery;
    }

    public void setSearchBarQuery(boolean searchBarQuery) {
        this.searchBarQuery = searchBarQuery;
    }
}
