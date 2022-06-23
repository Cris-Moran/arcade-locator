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

public class ProfileViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    // 4 - Live data
    private UserRepo userRepo;
    public MutableLiveData<String> mutableLiveUsername;
    public MutableLiveData<ParseFile> mutableLivePfp;

    public ProfileViewModel() {
        userRepo = new UserRepo();
    }

    public LiveData<String> getUsername() {
        if (mutableLiveUsername == null) {
            mutableLiveUsername = userRepo.getUsername();
        }
        return mutableLiveUsername;
    }

    public LiveData<ParseFile> getPfp() {
        if (mutableLivePfp == null) {
            mutableLivePfp = userRepo.getPfp();
        }
        return mutableLivePfp;
    }

    public void setPfp(ParseFile parseFile) {
        ParseFile result = userRepo.setPfp(parseFile);
        if (result != null) {
            mutableLivePfp.setValue(result);
        }
    }

    public void logOut() {
        userRepo.logOut();
    }

}
