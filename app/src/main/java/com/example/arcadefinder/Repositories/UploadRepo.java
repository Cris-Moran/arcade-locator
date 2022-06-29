package com.example.arcadefinder.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.Request;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class UploadRepo {

    public final String TAG = getClass().getSimpleName();

    public UploadModel createRequest(ParseGeoPoint coordinates, String locationName, String address, String gameTitle, String description, ParseFile image) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        saveRequest(coordinates, locationName, address, gameTitle, description, image, currentUser);
        MutableLiveData<UploadModel> mutableLiveData = new MutableLiveData<>();
        UploadModel uploadModel = new UploadModel();
        uploadModel.setCoordinates(coordinates);
        uploadModel.setLocationName(locationName);
        uploadModel.setAddress(address);
        uploadModel.setTitle(gameTitle);
        uploadModel.setDescription(description);
        uploadModel.setImage(image);
        uploadModel.setAuthor(currentUser);
        uploadModel.setIsVerified(false);
        return uploadModel;
    }

    private void saveRequest(ParseGeoPoint coordinates, String locationName, String address, String gameTitle, String description, ParseFile image, ParseUser currentUser) {
        Request request = new Request();
        request.setCoordinates(coordinates);
        request.setLocationName(locationName);
        request.setAddress(address);
        request.setTitle(gameTitle);
        request.setDescription(description);
        request.setImage(image);
        request.setAuthor(currentUser);
        request.setIsVerified(false);
        try {
            request.save();
            Log.i(TAG, "createRequest: Request saved successfully");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<UploadModel> getUpload() {
        MutableLiveData<UploadModel> mutableLiveData = new MutableLiveData<>();
        UploadModel uploadModel = new UploadModel();
        mutableLiveData.setValue(uploadModel);
        return mutableLiveData;
    }

}
