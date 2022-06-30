package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.RegisterModel;
import com.example.arcadefinder.Repositories.RegisterRepo;
import com.parse.ParseUser;

public class RegisterViewModel extends ViewModel {

    private RegisterRepo registerRepo;
    public MutableLiveData<RegisterModel> mutableLiveData;

    public RegisterViewModel() {
        registerRepo = new RegisterRepo();
    }

    public LiveData<RegisterModel> getRegisterModel() {
        if (mutableLiveData == null) {
            mutableLiveData = registerRepo.getRegisterModel();
        }
        return mutableLiveData;
    }

    public void registerUser(String username, String password) {
        registerRepo.registerUser(username, password, mutableLiveData);
    }
}
