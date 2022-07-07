package com.example.arcadefinder.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.Repositories.UploadRepo;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

public class UploadViewModel extends AndroidViewModel {

    MutableLiveData<UploadModel> mutableLiveData;
    UploadRepo uploadRepo;
    Context context;

    public UploadViewModel(@NonNull Application application) {
        super(application);
        uploadRepo = new UploadRepo();
        context = application.getApplicationContext();
    }


    public void createUpload(ParseGeoPoint location, String locationName, String address, String gameTitle, String description, ParseFile image) {
        uploadRepo.createRequest(location, locationName, address, gameTitle, description, image, mutableLiveData);
    }

    public LiveData<UploadModel> getUpload() {
        if (mutableLiveData == null) {
            mutableLiveData = uploadRepo.getUpload();
        }
        return mutableLiveData;
    }

    public void initPlaces() {
        uploadRepo.initPlaces(context);
    }

    public void getPlace(AutocompleteSupportFragment fragmentAddress) {
        uploadRepo.getPlace(fragmentAddress, mutableLiveData);
    }
}
