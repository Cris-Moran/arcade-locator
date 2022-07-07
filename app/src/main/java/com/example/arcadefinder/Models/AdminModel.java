package com.example.arcadefinder.Models;

import com.example.arcadefinder.GameLocation;

import java.util.List;

public class AdminModel {

    List<GameLocation> locations;

    public List<GameLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<GameLocation> locations) {
        this.locations = locations;
    }
}
