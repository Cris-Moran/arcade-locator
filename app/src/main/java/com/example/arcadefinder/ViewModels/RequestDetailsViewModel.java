package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.example.arcadefinder.Repositories.RequestDetailsRepo;

public class RequestDetailsViewModel extends ViewModel {
    MutableLiveData<RequestDetailsModel> mutableLiveData;
    RequestDetailsRepo requestDetailsRepo;

    public RequestDetailsViewModel() {
        requestDetailsRepo = new RequestDetailsRepo();
    }

    public LiveData<RequestDetailsModel> getRequestDetailsModel(GameLocation gameLocation) {
        if (mutableLiveData == null) {
            mutableLiveData = requestDetailsRepo.getRequestDetailsModel(gameLocation);
        }
        return mutableLiveData;
    }

    public void deleteLocation(GameLocation gameLocation) {
        requestDetailsRepo.deleteLocation(gameLocation, mutableLiveData);
    }
}
