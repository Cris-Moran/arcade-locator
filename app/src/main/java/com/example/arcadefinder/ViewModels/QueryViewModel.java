package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.Models.QueryModel;
import com.example.arcadefinder.Repositories.QueryRepo;

import org.json.JSONObject;

public class QueryViewModel extends ViewModel {

    MutableLiveData<QueryModel> mutableLiveData;
    QueryRepo queryRepo;

    public QueryViewModel() {
        queryRepo = new QueryRepo();
    }

    public LiveData<QueryModel> getQueryModel() {
        if (mutableLiveData == null) {
            mutableLiveData = queryRepo.getQueryModel();
        }
        return mutableLiveData;
    }

    public void getLocations(String query) {
        queryRepo.getLocations(query, mutableLiveData);
    }
}
