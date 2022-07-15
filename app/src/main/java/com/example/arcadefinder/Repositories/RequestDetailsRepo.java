package com.example.arcadefinder.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

public class RequestDetailsRepo {
    public MutableLiveData<RequestDetailsModel> getRequestDetailsModel(ParseGameLocation parseGameLocation) {
        String title = parseGameLocation.getTitle();
        String addressText = parseGameLocation.getLocationName() + "\n" + parseGameLocation.getAddress();
        String description = parseGameLocation.getDescription();
        ParseFile image = parseGameLocation.getImage();

        RequestDetailsModel requestDetailsModel = new RequestDetailsModel();
        requestDetailsModel.setTitle(title);
        requestDetailsModel.setAddressText(addressText);
        requestDetailsModel.setDescription(description);
        requestDetailsModel.setImage(image);

        MutableLiveData<RequestDetailsModel> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(requestDetailsModel);
        return mutableLiveData;
    }

    public void deleteLocation(ParseGameLocation parseGameLocation, MutableLiveData<RequestDetailsModel> mutableLiveData) {
        parseGameLocation.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
                requestDetailsModel.setDeleted(true);
                mutableLiveData.setValue(requestDetailsModel);
            }
        });
    }
}
