package com.example.arcadefinder;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.ParseFile;

public class ProfileViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    // 4 - Live data
    private UserRepo userRepo;
    public MutableLiveData<String> mutableLiveUsername;
    public MutableLiveData<ParseFile> mutableLivePfp;

    public ProfileViewModel() {
        userRepo = new UserRepo();
        mutableLiveUsername = (MutableLiveData<String>) userRepo.getUsername();
        mutableLivePfp = (MutableLiveData<ParseFile>) userRepo.getPfp();
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

    public void setUrl(String url) {
        mutableLiveUsername.postValue(url);
    }

    public void setPfp(ParseFile parseFile) {
        ParseFile result = userRepo.setPfp(parseFile);
        if (result != null) {
            mutableLivePfp.postValue(result);
        }
    }

    public void logOut() {
        userRepo.logOut();
    }

}
