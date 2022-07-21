package com.example.arcadefinder.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.GameLocationModel;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.example.arcadefinder.Repositories.RequestDetailsRepo;

public class RequestDetailsViewModel extends AndroidViewModel {
    MutableLiveData<RequestDetailsModel> mutableLiveData;
    RequestDetailsRepo requestDetailsRepo;

    public RequestDetailsViewModel(@NonNull Application application) {
        super(application);
        requestDetailsRepo = new RequestDetailsRepo(application);
    }

    public LiveData<RequestDetailsModel> getRequestDetailsModel() {
        if (mutableLiveData == null) {
            mutableLiveData = requestDetailsRepo.getRequestDetailsModel();
        }
        return mutableLiveData;
    }

    public void deleteLocationOnline(GameLocationModel gameLocationModel) {
        requestDetailsRepo.deleteLocationOnline(gameLocationModel, mutableLiveData);
    }

    public void setDeleted(boolean b) {
        RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
        requestDetailsModel.setDeleted(b);
        mutableLiveData.setValue(requestDetailsModel);
    }

    public void verifyLocation(GameLocationModel gameLocationModel) {
        requestDetailsRepo.verifyLocation(gameLocationModel, mutableLiveData);
    }

    public void setVerified(boolean b) {
        RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
        requestDetailsModel.setVerified(b);
        mutableLiveData.setValue(requestDetailsModel);
    }
}
