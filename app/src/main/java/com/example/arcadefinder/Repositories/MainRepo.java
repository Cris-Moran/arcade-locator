package com.example.arcadefinder.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.MainModel;
import com.parse.ParseUser;

public class MainRepo {

    public MutableLiveData<MainModel> getUser() {
        final MutableLiveData<MainModel> mutableLiveData = new MutableLiveData<>();
        MainModel mainModel = new MainModel();
        ParseUser parseUser = ParseUser.getCurrentUser();
        mainModel.setUser(parseUser);
        mutableLiveData.setValue(mainModel);
        return mutableLiveData;
    }
}
