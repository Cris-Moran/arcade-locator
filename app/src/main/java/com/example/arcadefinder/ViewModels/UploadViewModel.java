package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.Repositories.UploadRepo;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

public class UploadViewModel extends ViewModel {

    MutableLiveData<UploadModel> mutableLiveData;
    UploadRepo uploadRepo;

    public UploadViewModel() {
        uploadRepo = new UploadRepo();
    }

    public void createUpload(ParseGeoPoint location, String gameTitle, ParseFile image, String description) {
        mutableLiveData.setValue(uploadRepo.createRequest(location, gameTitle, image, description));
    }

    public LiveData<UploadModel> getUpload() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            mutableLiveData = uploadRepo.getUpload();
        }
        return mutableLiveData;
    }

}
