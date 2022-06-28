package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.LoginModel;
import com.example.arcadefinder.Repositories.LoginRepo;
import com.parse.ParseUser;

public class LoginViewModel extends ViewModel {

    private LoginRepo loginRepo;
    MutableLiveData<LoginModel> mutableLiveData;

    public LoginViewModel() {
        loginRepo = new LoginRepo();
    }

    public LiveData<LoginModel> getUser() {
        if (mutableLiveData == null) {
            mutableLiveData = loginRepo.getUser();
        }
        return mutableLiveData;
    }

    public boolean logIn(String username, String password) {
        ParseUser loggedInUser = loginRepo.logIn(username, password);
        LoginModel loginModel = mutableLiveData.getValue();
        loginModel.setUser(loggedInUser);
        mutableLiveData.setValue(loginModel);
        if (loggedInUser == null) {
            return false;
        }
        return true;
    }

}
