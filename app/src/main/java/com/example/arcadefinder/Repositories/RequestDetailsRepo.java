package com.example.arcadefinder.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.parse.DeleteCallback;
import com.parse.ParseException;
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

    public void deleteLocation(GameLocation gameLocation, MutableLiveData<RequestDetailsModel> mutableLiveData) {
        gameLocation.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
                requestDetailsModel.setDeleted(true);
                mutableLiveData.setValue(requestDetailsModel);
            }
        });
    }
}
