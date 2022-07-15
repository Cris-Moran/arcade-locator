package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.MainModel;
import com.example.arcadefinder.Repositories.MainRepo;

public class MainViewModel extends ViewModel {
    MainRepo mainRepo;
    MutableLiveData<MainModel> mutableLiveData;

    public MainViewModel() {
        mainRepo = new MainRepo();
    }

    public LiveData<MainModel> getUser() {
        if (mutableLiveData == null) {
            mutableLiveData = mainRepo.getUser();
        }
        return mutableLiveData;
    }

    public void verifyLocation(ParseGameLocation parseGameLocation) {
        mainRepo.verifyLocation(parseGameLocation, mutableLiveData);
    }

}
