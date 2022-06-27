package com.example.arcadefinder.ViewModels;

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
        uploadRepo.createRequest(location, gameTitle, image, description);
        UploadModel uploadModel = new UploadModel();
        uploadModel.setLocation(location);
        uploadModel.setTitle(gameTitle);
        uploadModel.setImage(image);
        uploadModel.setDescription(description);
        mutableLiveData.postValue(uploadModel);
    }

    public MutableLiveData<UploadModel> getUpload() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            mutableLiveData = uploadRepo.getUpload();
        }
        return mutableLiveData;
    }
}
