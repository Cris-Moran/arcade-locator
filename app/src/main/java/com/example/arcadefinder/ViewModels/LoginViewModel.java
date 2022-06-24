package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Repositories.UserRepo;
import com.parse.ParseUser;

public class LoginViewModel extends ViewModel {

    private UserRepo userRepo;
    MutableLiveData<ParseUser> mutableLiveUser;

    public LoginViewModel() {
        userRepo = new UserRepo();
    }

    public LiveData<ParseUser> getUser() {
        if (mutableLiveUser == null) {
            mutableLiveUser = userRepo.getUser();
        }
        return mutableLiveUser;
    }

    public void logIn(String username, String password) {
        userRepo.logIn(username, password);
    }
}
