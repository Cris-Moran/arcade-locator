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

    public MutableLiveData<UploadModel> createRequest(ParseGeoPoint location, String gameTitle, ParseFile image, String description) {
        Request request = new Request();
        request.setImage(image);
        request.setLocation(location);
        request.setTitle(gameTitle);
        request.setDescription(description);
        ParseUser currentUser = ParseUser.getCurrentUser();
        request.setAuthor(currentUser);
        request.setIsVerified(false);
        try {
            request.save();
            Log.i(TAG, "createRequest: Request saved successfully");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MutableLiveData<UploadModel> mutableLiveData = new MutableLiveData<>();
        UploadModel uploadModel = new UploadModel();
        uploadModel.setImage(image);
        uploadModel.setLocation(location);
        uploadModel.setTitle(gameTitle);
        uploadModel.setDescription(description);
        uploadModel.setAuthor(currentUser);
        uploadModel.setVerified(false);
        mutableLiveData.setValue(uploadModel);
        return mutableLiveData;
    }

    public MutableLiveData<UploadModel> getUpload() {
        MutableLiveData<UploadModel> mutableLiveData = new MutableLiveData<>();
        UploadModel uploadModel = new UploadModel();
        mutableLiveData.setValue(uploadModel);
        return mutableLiveData;
    }

}
