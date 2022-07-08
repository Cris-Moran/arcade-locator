package com.example.arcadefinder.Repositories;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Activities.MainActivity;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.MainModel;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainRepo {

    public String TAG = getClass().getSimpleName();

    public MutableLiveData<MainModel> getUser() {
        final MutableLiveData<MainModel> mutableLiveData = new MutableLiveData<>();
        MainModel mainModel = new MainModel();
        ParseUser parseUser = ParseUser.getCurrentUser();
        mainModel.setUser(parseUser);
        mutableLiveData.setValue(mainModel);
        return mutableLiveData;
    }

    public void verifyLocation(GameLocation gameLocation, MutableLiveData<MainModel> mutableLiveData) {
        gameLocation.setIsVerified(true);
        gameLocation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "done: successfully verified location");
                    MainModel mainModel = mutableLiveData.getValue();
                    mainModel.setVerifyStatus(true);
                    mutableLiveData.setValue(mainModel);
                } else {
                    Log.e(TAG, "done: error verifying location", e);
                }
            }
        });
    }
}
