package com.example.arcadefinder.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.parse.ParseFile;

public class RequestDetailsRepo {
    public MutableLiveData<RequestDetailsModel> getRequestDetailsModel(GameLocation gameLocation) {
        String title = gameLocation.getTitle();
        String addressText = gameLocation.getLocationName() + "\n" + gameLocation.getAddress();
        String description = gameLocation.getDescription();
        ParseFile image = gameLocation.getImage();

        RequestDetailsModel requestDetailsModel = new RequestDetailsModel();
        requestDetailsModel.setTitle(title);
        requestDetailsModel.setAddressText(addressText);
        requestDetailsModel.setDescription(description);
        requestDetailsModel.setImage(image);

        MutableLiveData<RequestDetailsModel> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(requestDetailsModel);
        return mutableLiveData;
    }
}
