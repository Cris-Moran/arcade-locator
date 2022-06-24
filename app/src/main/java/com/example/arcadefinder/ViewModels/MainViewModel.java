package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Repositories.UserRepo;
import com.parse.ParseUser;

public class MainViewModel extends ViewModel {
    UserRepo userRepo;
    MutableLiveData<ParseUser> mutableLiveData;

    public MainViewModel() {
        userRepo = new UserRepo();
    }

    public LiveData<ParseUser> getUser() {
        if (mutableLiveData == null) {
            mutableLiveData = userRepo.getUser();
        }
        return mutableLiveData;
    }
}
