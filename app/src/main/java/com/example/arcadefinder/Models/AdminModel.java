package com.example.arcadefinder.Models;

import com.example.arcadefinder.ParseGameLocation;

import java.util.List;

public class AdminModel {

    List<ParseGameLocation> locations;

    public List<ParseGameLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<ParseGameLocation> locations) {
        this.locations = locations;
    }
}
