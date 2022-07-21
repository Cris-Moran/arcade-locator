package com.example.arcadefinder.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.GameLocationDao;
import com.example.arcadefinder.GameLocationDatabase;
import com.example.arcadefinder.Models.GameLocationModel;
import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.RequestDetailsModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.RoomGameLocation;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class RequestDetailsRepo {

    private GameLocationDao gameLocationDao;

    public RequestDetailsRepo(Application application) {
        GameLocationDatabase database = GameLocationDatabase.getInstance(application);
        gameLocationDao = database.gameLocationDao();
    }


    public void deleteLocationOnline(GameLocationModel gameLocationModel, MutableLiveData<RequestDetailsModel> mutableLiveData) {
        ParseQuery<ParseGameLocation> query = ParseQuery.getQuery(ParseGameLocation.class);
        query.whereEqualTo(ParseGameLocation.KEY_ID, gameLocationModel.getId());
        query.getFirstInBackground(new GetCallback<ParseGameLocation>() {
            @Override
            public void done(ParseGameLocation object, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
                        requestDetailsModel.setDeleted(true);
                        mutableLiveData.setValue(requestDetailsModel);
                    }
                });
            }
        });
    }

    public MutableLiveData<RequestDetailsModel> getRequestDetailsModel() {
        final MutableLiveData<RequestDetailsModel> mutableLiveData = new MutableLiveData<>();
        RequestDetailsModel requestDetailsModel = new RequestDetailsModel();
        mutableLiveData.setValue(requestDetailsModel);
        return mutableLiveData;
    }

    public void verifyLocation(GameLocationModel gameLocationModel, MutableLiveData<RequestDetailsModel> mutableLiveData) {
        ParseQuery<ParseGameLocation> query = ParseQuery.getQuery(ParseGameLocation.class);
        query.whereEqualTo(ParseGameLocation.KEY_ID, gameLocationModel.getId());
        query.findInBackground(new FindCallback<ParseGameLocation>() {
            @Override
            public void done(List<ParseGameLocation> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                ParseGameLocation location = objects.get(0);
                location.setIsVerified(true);
                location.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
                        requestDetailsModel.setVerified(true);
                        mutableLiveData.setValue(requestDetailsModel);
                    }
                });
            }
        });
    }
}
