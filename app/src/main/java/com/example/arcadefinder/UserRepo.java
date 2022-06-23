package com.example.arcadefinder;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class UserRepo {

    private final String TAG = getClass().getSimpleName();

    public MutableLiveData<ParseUser> getUser() {
        final MutableLiveData<ParseUser> mutableLiveData = new MutableLiveData<>();
        ParseUser parseUser = ParseUser.getCurrentUser();
        mutableLiveData.setValue(parseUser);
        return mutableLiveData;
    }

    public MutableLiveData<ParseUser> setUser(String username, String password) {
        final MutableLiveData<ParseUser> mutableLiveData = new MutableLiveData<>();
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while signing up: ", e);
                    return;
                }
                Log.i(TAG, "signed up successfully!");
            }
        });
        mutableLiveData.setValue(newUser);
        return mutableLiveData;
    }

    public void logIn(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while logging in: ", e);
                    return;
                }
                Log.i(TAG, "logged in successfully!");
            }
        });
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
                    Log.e(TAG, "error while saving profile image");
                    return;
                }
                Log.i(TAG, "set up profile image successfully!");
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
