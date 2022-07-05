package com.example.arcadefinder.Models;

import com.example.arcadefinder.GameLocation;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import java.util.Map;

public class MapModel {

//    private GoogleMap map;
    private List<GameLocation> locationList;

//    public GoogleMap getMap() {
//        return map;
//    }
//
//    public void setMap(GoogleMap map) {
//        this.map = map;
//    }

    public List<GameLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<GameLocation> locationList) {
        this.locationList = locationList;
    }
}
