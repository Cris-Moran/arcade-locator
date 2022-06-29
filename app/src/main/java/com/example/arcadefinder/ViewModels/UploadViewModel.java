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

    public void createUpload(ParseGeoPoint location, String locationName, String address, String gameTitle, String description, ParseFile image) {
        mutableLiveData.setValue(uploadRepo.createRequest(location, locationName, address, gameTitle, description, image));
    }

    public LiveData<UploadModel> getUpload() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            mutableLiveData = uploadRepo.getUpload();
        }
        return mutableLiveData;
    }

}
