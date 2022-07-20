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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

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
        query.findInBackground(new FindCallback<ParseGameLocation>() {
            @Override
            public void done(List<ParseGameLocation> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                ParseGameLocation location = objects.get(0);
                location.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        new DeleteLocationAsyncTask(gameLocationDao, new AsyncResponse() {
                            @Override
                            public void onFinished() {
                                RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
                                requestDetailsModel.setDeleted(true);
                                mutableLiveData.setValue(requestDetailsModel);
                            }
                        }).execute(gameLocationModel.getId());
                    }
                });
            }
        });
    }

    public void deleteLocationOffline(GameLocationModel gameLocationModel, MutableLiveData<RequestDetailsModel> mutableLiveData) {
        new DeleteLocationAsyncTask(gameLocationDao, new AsyncResponse() {
            @Override
            public void onFinished() {
                RequestDetailsModel requestDetailsModel = mutableLiveData.getValue();
                requestDetailsModel.setDeleted(true);
                mutableLiveData.setValue(requestDetailsModel);
            }
        }).execute(gameLocationModel.getId());
    }

    public MutableLiveData<RequestDetailsModel> getRequestDetailsModel() {
        final MutableLiveData<RequestDetailsModel> mutableLiveData = new MutableLiveData<>();
        RequestDetailsModel requestDetailsModel = new RequestDetailsModel();
        mutableLiveData.setValue(requestDetailsModel);
        return mutableLiveData;
    }

    private static class DeleteLocationAsyncTask extends AsyncTask<String, Void, Void> {
        private GameLocationDao gameLocationDao;
        private AsyncResponse delegate;

        private DeleteLocationAsyncTask(GameLocationDao gameLocationDao, AsyncResponse delegate) {
            this.gameLocationDao = gameLocationDao;
            this.delegate = delegate;
        }

        @Override
        protected Void doInBackground(String... strings) {
            gameLocationDao.delete(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            delegate.onFinished();
        }
    }

    private interface AsyncResponse {
        void onFinished();
    }
}
