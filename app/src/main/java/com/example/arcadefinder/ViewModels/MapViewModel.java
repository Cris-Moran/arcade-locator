package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.MapModel;
import com.example.arcadefinder.Repositories.MapRepo;

public class MapViewModel {

    MapRepo mapRepo;
    MutableLiveData<MapModel> mutableLiveData;

}
