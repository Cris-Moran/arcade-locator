package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Repositories.UserRepo;
import com.parse.ParseUser;

public class RegisterViewModel extends ViewModel {

    private UserRepo userRepo;
    public MutableLiveData<ParseUser> mutableLiveData;

    public RegisterViewModel() {
        userRepo = new UserRepo();
    }

    public boolean registerUser(String username, String password) {
        return userRepo.registerUser(username, password);
    }
}
