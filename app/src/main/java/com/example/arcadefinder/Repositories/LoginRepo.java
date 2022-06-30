package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.LoginModel;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginRepo {

    private final String TAG = getClass().getSimpleName();

    public void logIn(String username, String password, MutableLiveData<LoginModel> mutableLiveData) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    LoginModel loginModel = mutableLiveData.getValue();
                    loginModel.setUser(user);
                    mutableLiveData.setValue(loginModel);
                    Log.i(TAG, "logged in successfully");
                } else {
                    Log.e(TAG, "Error while logging in: ", e);
                }
            }
        });
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
