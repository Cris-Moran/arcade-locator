package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.ProfileModel;
import com.example.arcadefinder.Repositories.ProfileRepo;
import com.parse.ParseFile;

public class ProfileViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    // 4 - Live data
    private ProfileRepo profileRepo;
    public MutableLiveData<ProfileModel> mutableLiveData;

    public ProfileViewModel() {
        profileRepo = new ProfileRepo();
    }

    public LiveData<ProfileModel> getProfile() {
        if (mutableLiveData == null) {
            mutableLiveData = profileRepo.getProfile();
        }
        return mutableLiveData;
    }

    public void setUrl(String url) {
        ProfileModel profileModel = mutableLiveData.getValue();
        profileModel.setProfileImagePath(url);
        mutableLiveData.postValue(profileModel);
    }

    public void setPfp(ParseFile parseFile) {
        ParseFile result = profileRepo.setPfp(parseFile);
        if (result != null) {
            ProfileModel profileModel = mutableLiveData.getValue();
            profileModel.setProfileImage(parseFile);
            mutableLiveData.postValue(profileModel);
        }
    }

    public void logOut() {
        profileRepo.logOut();
    }

}
