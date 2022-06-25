package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.LoginModel;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginRepo {

    private final String TAG = getClass().getSimpleName();

    public ParseUser logIn(String username, String password) {
        try {
            ParseUser.logIn(username, password);
            Log.i(TAG, "logged in successfully!");
            return ParseUser.getCurrentUser();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MutableLiveData<LoginModel> getUser() {
        final MutableLiveData<LoginModel> mutableLiveData = new MutableLiveData<>();
        LoginModel loginModel = new LoginModel();
        ParseUser parseUser = ParseUser.getCurrentUser();
        loginModel.setUser(parseUser);
        mutableLiveData.setValue(loginModel);
        return mutableLiveData;
    }
}
