package com.example.arcadefinder.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.Repositories.UploadRepo;
import com.example.arcadefinder.UploadFragment;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
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

    public void initPlaces(Context context) {
        uploadRepo.initPlaces(context);
    }

    public void initSearchBar(AutocompleteSupportFragment fragmentAddress) {
        uploadRepo.initSearchBar(fragmentAddress);
    }
}
