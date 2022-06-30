package com.example.arcadefinder.Repositories;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.GameLocation;
import com.google.android.libraries.places.api.Places;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class UploadRepo {

    public final String TAG = getClass().getSimpleName();

    public UploadModel createRequest(ParseGeoPoint coordinates, String locationName, String address, String gameTitle, String description, ParseFile image) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        boolean status = saveRequest(coordinates, locationName, address, gameTitle, description, image, currentUser);
        UploadModel uploadModel = new UploadModel();
        uploadModel.setCoordinates(coordinates);
        uploadModel.setLocationName(locationName);
        uploadModel.setAddress(address);
        uploadModel.setTitle(gameTitle);
        uploadModel.setDescription(description);
        uploadModel.setImage(image);
        uploadModel.setAuthor(currentUser);
        uploadModel.setIsVerified(false);
        uploadModel.setStatus(status);
        return uploadModel;
    }

    private boolean saveRequest(ParseGeoPoint coordinates, String locationName, String address, String gameTitle, String description, ParseFile image, ParseUser currentUser) {
        GameLocation request = new GameLocation();
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
            return true;
        } catch (ParseException e) {
            Log.e(TAG, "saveRequest: Issue with saving request: ", e);
            return false;
        }
    }

    public MutableLiveData<UploadModel> getUpload() {
        MutableLiveData<UploadModel> mutableLiveData = new MutableLiveData<>();
        UploadModel uploadModel = new UploadModel();
        mutableLiveData.setValue(uploadModel);
        return mutableLiveData;
    }

    public void initPlaces(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = info.metaData;
        String apiKey = bundle.getString("com.google.android.geo.API_KEY");

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(context.getApplicationContext(), apiKey);
        }

        Places.createClient(context);
    }

}
