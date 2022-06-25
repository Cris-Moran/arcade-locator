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

    public boolean registerUser(String username, String password) {
        ParseUser registeredUser = registerRepo.registerUser(username, password);
        RegisterModel registerModel = mutableLiveData.getValue();
        registerModel.setUser(registeredUser);
        mutableLiveData.postValue(registerModel);
        if (registeredUser == null) {
            return false;
        }
        return true;
    }
}
