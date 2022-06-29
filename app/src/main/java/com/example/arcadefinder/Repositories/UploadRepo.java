package com.example.arcadefinder.Repositories;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.Request;
import com.example.arcadefinder.UploadFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Arrays;

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

    public void initSearchBar(AutocompleteSupportFragment fragmentAddress) {
        fragmentAddress.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        fragmentAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                UploadFragment.setCoordinates(place.getLatLng());
                UploadFragment.setLocationName(place.getName());
                UploadFragment.setAddress(place.getAddress());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }
}
