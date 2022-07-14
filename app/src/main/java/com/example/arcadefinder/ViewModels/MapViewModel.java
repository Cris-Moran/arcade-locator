package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.MapModel;
import com.example.arcadefinder.Repositories.MapRepo;
import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.List;

public class MapViewModel extends ViewModel {

    MapRepo mapRepo;
    MutableLiveData<MapModel> mutableLiveData;

    public MapViewModel() {
        mapRepo = new MapRepo();
    }

    public LiveData<MapModel> getMapModel() {
        if (mutableLiveData == null) {
            mutableLiveData = mapRepo.getMapModel();
        }
        return mutableLiveData;
    }

    public void queryLocations(String gameTitle, double radius, ParseGeoPoint currentLocation) {
        MapModel mapModel = mutableLiveData.getValue();
        mapModel.setQuery(gameTitle);
        mutableLiveData.setValue(mapModel);
        mapRepo.queryLocations(gameTitle, radius, currentLocation, mutableLiveData);
    }

    public void setLocationPermission(boolean b) {
        MapModel mapModel = mutableLiveData.getValue();
        mapModel.setLocationPermission(b);
        mutableLiveData.setValue(mapModel);
    }

    public HashMap<String, Object> getLocationFields(GameLocation gameLocation) {
        return mapRepo.getLocationFields(gameLocation);
    }

    public String getLocationId(GameLocation gameLocation) {
        return mapRepo.getLocationId(gameLocation);
    }

    public void queryLocationById(List<String> ids) {
        mapRepo.queryLocationById(ids, mutableLiveData);
    }
}
