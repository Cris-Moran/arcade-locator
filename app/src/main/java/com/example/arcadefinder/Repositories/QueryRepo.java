package com.example.arcadefinder.Repositories;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.arcadefinder.Models.QueryModel;

import org.json.JSONObject;

import okhttp3.Headers;

public class QueryRepo {

    public MutableLiveData<QueryModel> getQueryModel() {
        final MutableLiveData<QueryModel> mutableLiveData = new MutableLiveData<>();
        QueryModel queryModel = new QueryModel();
        mutableLiveData.setValue(queryModel);
        return mutableLiveData;
    }

    public void getLocations(String query, MutableLiveData<QueryModel> mutableLiveData) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(query, new JsonHttpResponseHandler() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                QueryModel queryModel = mutableLiveData.getValue();
                queryModel.setResponse(jsonObject);
                mutableLiveData.setValue(queryModel);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            }
        });
    }
}
