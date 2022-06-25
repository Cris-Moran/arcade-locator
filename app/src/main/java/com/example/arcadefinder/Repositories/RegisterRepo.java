package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.MainModel;
import com.example.arcadefinder.Models.RegisterModel;
import com.parse.ParseException;
import com.parse.ParseUser;

public class RegisterRepo {

    public final String TAG = getClass().getSimpleName();

    public MutableLiveData<RegisterModel> getRegisterModel() {
        final MutableLiveData<RegisterModel> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(new RegisterModel());
        return mutableLiveData;
    }

    public ParseUser registerUser(String username, String password) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        try {
            newUser.signUp();
            Log.i(TAG, "signed up successfully!");
            return ParseUser.getCurrentUser();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
