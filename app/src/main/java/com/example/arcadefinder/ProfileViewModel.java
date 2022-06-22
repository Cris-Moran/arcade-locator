package com.example.arcadefinder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class ProfileViewModel extends AndroidViewModel {
    private static final String TAG = "ProfileViewModel";

    // 4 - Live data
    MutableLiveData<ParseUser> mutableLiveData;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mutableLiveData = new MutableLiveData<>();
    }

    public void getUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        mutableLiveData.setValue(currentUser);
    }

}
