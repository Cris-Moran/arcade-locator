package com.example.arcadefinder.Repositories;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.ParseGameLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UploadRepo {

    public final String TAG = getClass().getSimpleName();

    public void createRequest(ParseGeoPoint coordinates, String locationName, String address, String gameTitle, String description, ParseFile image, MutableLiveData<UploadModel> mutableLiveData) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGameLocation request = new ParseGameLocation();
        request.setCoordinates(coordinates);
        request.setLocationName(locationName);
        request.setAddress(address);
        request.setTitle(gameTitle);
        request.setDescription(description);
        request.setImage(image);
        request.setAuthor(currentUser);
        request.setIsVerified(false);
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    updateModel(true, false, mutableLiveData);
                    Log.i(TAG, "createRequest: Request saved successfully");
                } else {
                    Log.e(TAG, "saveRequest: Issue with saving request: ", e);
                    updateModel(false, true, mutableLiveData);
                }
            }
        });
    }

    private void updateModel(boolean success, boolean errorUploading, MutableLiveData<UploadModel> mutableLiveData) {
        UploadModel uploadModel = mutableLiveData.getValue();
        uploadModel.setCoordinates(null);
        uploadModel.setLocationName("");
        uploadModel.setAddress("");
        uploadModel.setTitle("");
        uploadModel.setDescription("");
        uploadModel.setImage(null);
        uploadModel.setAuthor(null);
        uploadModel.setUploadStatus(success);
        uploadModel.setErrorUploading(errorUploading);
        mutableLiveData.setValue(uploadModel);
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

    public void getPlace(AutocompleteSupportFragment fragmentAddress, MutableLiveData<UploadModel> mutableLiveData) {
        fragmentAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                UploadModel uploadModel = mutableLiveData.getValue();
                LatLng latLng = place.getLatLng();
                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(latLng.latitude, latLng.longitude);
                uploadModel.setUploadStatus(false);
                uploadModel.setErrorUploading(false);
                uploadModel.setCoordinates(parseGeoPoint);
                uploadModel.setLocationName(place.getName());
                uploadModel.setAddress(place.getAddress());
                mutableLiveData.setValue(uploadModel);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void setUploadStatus(boolean b, MutableLiveData<UploadModel> mutableLiveData) {
        UploadModel uploadModel = mutableLiveData.getValue();
        uploadModel.setUploadStatus(b);
        mutableLiveData.setValue(uploadModel);
    }

    public void setErrorUploading(boolean b, MutableLiveData<UploadModel> mutableLiveData) {
        UploadModel uploadModel = mutableLiveData.getValue();
        uploadModel.setErrorUploading(b);
        mutableLiveData.setValue(uploadModel);
    }

    public void setCoordinates(ParseGeoPoint coords, MutableLiveData<UploadModel> mutableLiveData) {
        UploadModel uploadModel = mutableLiveData.getValue();
        uploadModel.setCoordinates(coords);
        mutableLiveData.setValue(uploadModel);
    }
}
