package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.MainModel;
import com.example.arcadefinder.Models.RegisterModel;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterRepo {

    public final String TAG = getClass().getSimpleName();

    public MutableLiveData<RegisterModel> getRegisterModel() {
        final MutableLiveData<RegisterModel> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(new RegisterModel());
        return mutableLiveData;
    }

    public void registerUser(String username, String password, MutableLiveData<RegisterModel> mutableLiveData) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    RegisterModel registerModel = mutableLiveData.getValue();
                    registerModel.setUser(ParseUser.getCurrentUser());
                    mutableLiveData.setValue(registerModel);
                }
            }
        });
    }
}
