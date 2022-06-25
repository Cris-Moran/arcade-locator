package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.ProfileModel;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileRepo {

    public final String TAG = getClass().getSimpleName();

    public MutableLiveData<ProfileModel> getProfile() {
        final MutableLiveData<ProfileModel> mutableLiveData = new MutableLiveData<>();
        ProfileModel profileModel = new ProfileModel();

        ParseUser currentUser = ParseUser.getCurrentUser();

        String username = currentUser.getUsername();
        ParseFile profileImage = currentUser.getParseFile("profileImage");

        profileModel.setUsername(username);
        profileModel.setProfileImage(profileImage);

        mutableLiveData.setValue(profileModel);
        return mutableLiveData;
    }

    public ParseFile setPfp(ParseFile parseFile) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("profileImage", parseFile);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while saving profileModel image");
                    return;
                }
                Log.i(TAG, "set up profileModel image successfully!");
            }
        });
        return parseFile;
    }

    public void logOut() {
        ParseUser.logOut();
    }

}
