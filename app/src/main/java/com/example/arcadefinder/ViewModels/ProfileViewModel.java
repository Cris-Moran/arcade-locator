package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Repositories.UserRepo;
import com.parse.ParseFile;

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
        userRepo.setPfp(parseFile);
    }

    public void logOut() {
        userRepo.logOut();
    }

}
