package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.AdminModel;
import com.example.arcadefinder.Repositories.AdminRepo;

public class AdminViewModel extends ViewModel {

    MutableLiveData<AdminModel> mutableLiveData;
    AdminRepo adminRepo;

    public AdminViewModel() {
        adminRepo = new AdminRepo();
    }

    public LiveData<AdminModel> getAdminModel() {
        if (mutableLiveData == null) {
            mutableLiveData = adminRepo.getAdminModel();
        }
        return mutableLiveData;
    }

    public void queryRequests() {
        adminRepo.queryRequests(mutableLiveData);
    }
}
