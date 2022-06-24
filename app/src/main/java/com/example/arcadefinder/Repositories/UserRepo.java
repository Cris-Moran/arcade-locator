package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.ProfileModel;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class UserRepo {

    private final String TAG = getClass().getSimpleName();
    private ParseUser currentUser;

    public MutableLiveData<ParseUser> getUser() {
        final MutableLiveData<ParseUser> mutableLiveData = new MutableLiveData<>();
        ParseUser parseUser = ParseUser.getCurrentUser();
        mutableLiveData.setValue(parseUser);
        return mutableLiveData;
    }

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

    public boolean registerUser(String username, String password) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
//        newUser.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "error while signing up: ", e);
//                    return;
//                }
//                Log.i(TAG, "signed up successfully!");
//            }
//        });
        try {
            newUser.signUp();
            Log.i(TAG, "signed up successfully!");
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean logIn(String username, String password) {
//        ParseUser.logInInBackground(username, password, new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "error while logging in: ", e);
//                    return;
//                }
//                currentUser = user;
//                Log.i(TAG, "logged in successfully!");
//            }
//        });
        try {
            ParseUser.logIn(username, password);
            Log.i(TAG, "logged in successfully!");
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logOut() {
        ParseUser.logOut();
    }

    public MutableLiveData<ParseFile> getPfp() {
        final MutableLiveData<ParseFile> mutableLiveData = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseFile parseFile = currentUser.getParseFile("profileImage");
        mutableLiveData.setValue(parseFile);
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

    public MutableLiveData<String> getUsername() {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        mutableLiveData.setValue(currentUser.getUsername());
        return mutableLiveData;
    }

}
