package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.ProfileModel;
import com.example.arcadefinder.Repositories.UserRepo;
import com.parse.ParseFile;

public class ProfileViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    // 4 - Live data
    private UserRepo userRepo;
    public MutableLiveData<ProfileModel> mutableLiveData;

    public ProfileViewModel() {
        userRepo = new UserRepo();
    }

    public LiveData<ProfileModel> getProfile() {
        if (mutableLiveData == null) {
            mutableLiveData = userRepo.getProfile();
        }
        return mutableLiveData;
    }

    public void setUrl(String url) {
        ProfileModel profileModel = mutableLiveData.getValue();
        profileModel.setProfileImagePath(url);
        mutableLiveData.postValue(profileModel);
    }

    public void setPfp(ParseFile parseFile) {
        ParseFile result = userRepo.setPfp(parseFile);
        if (result != null) {
            ProfileModel profileModel = mutableLiveData.getValue();
            profileModel.setProfileImage(parseFile);
            mutableLiveData.postValue(profileModel);
        }
    }

    public void logOut() {
        userRepo.logOut();
    }

}
