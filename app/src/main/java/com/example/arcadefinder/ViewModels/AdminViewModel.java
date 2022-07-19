package com.example.arcadefinder.ViewModels;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.arcadefinder.Models.AdminModel;
import com.example.arcadefinder.Models.GameLocationModel;
import com.example.arcadefinder.Repositories.AdminRepo;
import com.example.arcadefinder.RoomGameLocation;

import java.util.ArrayList;
import java.util.List;

public class AdminViewModel extends AndroidViewModel {

    MutableLiveData<List<GameLocationModel>> mutableLiveData;
    AdminRepo adminRepo;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        adminRepo = new AdminRepo(application);
    }

    // Observe change to both parseGameLocation and allGameLocations
    public LiveData<List<GameLocationModel>> getGameLocations() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    public void queryRequests() {
        adminRepo.queryRequests(mutableLiveData);
    }

    public void getRequestsOffline() {
        adminRepo.getRequestsOffline(mutableLiveData);
    }

}
