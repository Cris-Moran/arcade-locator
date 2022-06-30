package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.MapModel;
import com.example.arcadefinder.Repositories.MapRepo;

public class MapViewModel extends ViewModel {

    MapRepo mapRepo;
    MutableLiveData<MapModel> mutableLiveData;


}
