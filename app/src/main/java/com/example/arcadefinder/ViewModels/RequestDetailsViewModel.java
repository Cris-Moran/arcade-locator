package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.example.arcadefinder.Repositories.RequestDetailsRepo;

public class RequestDetailsViewModel extends ViewModel {
    MutableLiveData<RequestDetailsModel> mutableLiveData;
    RequestDetailsRepo requestDetailsRepo;

    public RequestDetailsViewModel() {
        requestDetailsRepo = new RequestDetailsRepo();
    }

    public LiveData<RequestDetailsModel> getRequestDetailsModel(ParseGameLocation parseGameLocation) {
        if (mutableLiveData == null) {
            mutableLiveData = requestDetailsRepo.getRequestDetailsModel(parseGameLocation);
        }
        return mutableLiveData;
    }

    public void deleteLocation(ParseGameLocation parseGameLocation) {
        requestDetailsRepo.deleteLocation(parseGameLocation, mutableLiveData);
    }
}
