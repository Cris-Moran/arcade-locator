package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.LoginModel;
import com.example.arcadefinder.Repositories.LoginRepo;

public class LoginViewModel extends ViewModel {

    private LoginRepo loginRepo;
    MutableLiveData<LoginModel> mutableLiveData;

    public LoginViewModel() {
        loginRepo = new LoginRepo();
    }

    public LiveData<LoginModel> getLoginModel() {
        if (mutableLiveData == null) {
            mutableLiveData = loginRepo.getLoginModel();
        }
        return mutableLiveData;
    }

    public void logIn(String username, String password) {
        loginRepo.logIn(username, password, mutableLiveData);
    }

}
