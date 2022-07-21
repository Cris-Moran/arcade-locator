package com.example.arcadefinder.Repositories;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.arcadefinder.GameLocationDao;
import com.example.arcadefinder.GameLocationDatabase;
import com.example.arcadefinder.Models.GameLocationModel;
import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.RoomGameLocation;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AdminRepo {

    final String TAG = getClass().getSimpleName();

    private GameLocationDao gameLocationDao;
    private int offset = 0;

    public AdminRepo(Application application) {
        GameLocationDatabase database = GameLocationDatabase.getInstance(application);
        gameLocationDao = database.gameLocationDao();
    }

    public void queryRequests(boolean extendingFeed, MutableLiveData<List<GameLocationModel>> mutableLiveData) {
        // specify what type of data we want to query - Post.class
        ParseQuery<ParseGameLocation> query = ParseQuery.getQuery(ParseGameLocation.class);
        // include data referred by user key
        query.include(ParseGameLocation.KEY_AUTHOR);
        // limit query to latest 20 items
        query.setLimit(20);
        // for endless scrolling: if we are extending the feed than skip the first 20
        if (extendingFeed) {
            offset += 20;
            query.setSkip(offset);
        } else {
            query.setSkip(0);
        }
        // only get queries that haven't been verified
        query.whereEqualTo(ParseGameLocation.KEY_VERIFIED, false);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<ParseGameLocation>() {
            @Override
            public void done(List<ParseGameLocation> requests, ParseException e) {
                // check for errors
                if (e != null) {
                    e.printStackTrace();
                    return;
                }

                // save received posts to list and notify adapter of new data
                List<GameLocationModel> gameLocationModels = mutableLiveData.getValue();
                ArrayList<RoomGameLocation> newLocations = new ArrayList<>();
                for (ParseGameLocation location : requests) {
                    parseToModel(location, gameLocationModels);
                    saveToRoom(location, newLocations);
                }
                new InsertAllRequestsAsyncTask(gameLocationDao).execute(newLocations.toArray(new RoomGameLocation[newLocations.size()]));
                mutableLiveData.setValue(gameLocationModels);
            }
        });
    }

    private void parseToModel(ParseGameLocation location, List<GameLocationModel> gameLocationModels) {
        // Type conversions
        ParseGeoPoint coordinates = location.getCoordinates();
        double longitude = coordinates.getLongitude();
        double latitude = coordinates.getLatitude();
        // Convert parseFile to bitmap
        byte[] byteArray;
        try {
            byteArray = location.getImage().getData();
        } catch (ParseException ex) {
            ex.printStackTrace();
            return;
        }
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        GameLocationModel gameLocationModel = new GameLocationModel();
        gameLocationModel.setId(location.getObjectId());
        gameLocationModel.setCreatedAt(location.getCreatedAt());
        gameLocationModel.setUpdatedAt(location.getUpdatedAt());
        gameLocationModel.setLongitude(longitude);
        gameLocationModel.setLatitude(latitude);
        gameLocationModel.setLocationName(location.getLocationName());
        gameLocationModel.setAddress(location.getAddress());
        gameLocationModel.setTitle(location.getTitle());
        gameLocationModel.setDescription(location.getDescription());
        gameLocationModel.setImage(image);
        gameLocationModel.setVerified(location.getIsVerified());
        gameLocationModel.setUsername(location.getAuthor().getUsername());

        gameLocationModels.add(gameLocationModel);
    }

    private void saveToRoom(ParseGameLocation location, ArrayList<RoomGameLocation> newLocations) {
        // Convert parseFile to bitmap
        byte[] byteArray;
        try {
            byteArray = location.getImage().getData();
        } catch (ParseException ex) {
            ex.printStackTrace();
            return;
        }
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        // Get ParseGeoPoint
        ParseGeoPoint coordinates = location.getCoordinates();

        // Populate room object with appropriate fields
        newLocations.add(new RoomGameLocation(location.getObjectId(), location.getCreatedAt(), location.getUpdatedAt(),
                location.getLocationName(), location.getAddress(), location.getTitle(), location.getDescription(), image,
                location.getIsVerified(), location.getAuthor().getUsername(), coordinates.getLongitude(), coordinates.getLatitude()));
    }

    public void getRequestsOffline(MutableLiveData<List<GameLocationModel>> mutableLiveData) {
        GetAllRequestsAsyncTask getAllRequestsAsyncTask = new GetAllRequestsAsyncTask(gameLocationDao, new AsyncResponse() {
            @Override
            public void onFinished(List<RoomGameLocation> gameLocationModels) {
                List<GameLocationModel> gameLocationModel = new ArrayList<>();
                for (RoomGameLocation location : gameLocationModels) {
                    roomToModel(location, gameLocationModel);
                }
                mutableLiveData.setValue(gameLocationModel);
            }
        });
        getAllRequestsAsyncTask.execute();
    }

    private void roomToModel(RoomGameLocation location, List<GameLocationModel> gameLocationModels) {
        GameLocationModel gameLocationModel = new GameLocationModel();
        gameLocationModel.setId(location.getId());
        gameLocationModel.setCreatedAt(location.getCreatedAt());
        gameLocationModel.setUpdatedAt(location.getUpdatedAt());
        gameLocationModel.setLongitude(location.getLongitude());
        gameLocationModel.setLatitude(location.getLatitude());
        gameLocationModel.setLocationName(location.getLocationName());
        gameLocationModel.setAddress(location.getAddress());
        gameLocationModel.setTitle(location.getTitle());
        gameLocationModel.setDescription(location.getDescription());
        gameLocationModel.setImage(location.getImage());
        gameLocationModel.setVerified(location.isVerified());
        gameLocationModel.setUsername(location.getUsername());
        gameLocationModels.add(gameLocationModel);
    }

    public MutableLiveData<List<GameLocationModel>> getGameLocation() {
        final MutableLiveData<List<GameLocationModel>> mutableLiveData = new MutableLiveData<>();
        List<GameLocationModel> locations = new ArrayList<>();
        mutableLiveData.setValue(locations);
        return mutableLiveData;
    }

    private static class InsertAllRequestsAsyncTask extends AsyncTask<RoomGameLocation, Void, Void> {
        private GameLocationDao gameLocationDao;

        private InsertAllRequestsAsyncTask(GameLocationDao gameLocationDao) {
            this.gameLocationDao = gameLocationDao;
        }

        @Override
        protected Void doInBackground(RoomGameLocation... roomGameLocations) {
            gameLocationDao.insertAll(roomGameLocations);
            return null;
        }
    }

    private static class GetAllRequestsAsyncTask extends AsyncTask<Void, Void, List<RoomGameLocation>> {
        private GameLocationDao gameLocationDao;
        private AsyncResponse delegate;

        private GetAllRequestsAsyncTask(GameLocationDao gameLocationDao, AsyncResponse asyncResponse) {
            this.gameLocationDao = gameLocationDao;
            this.delegate = asyncResponse;
        }

        @Override
        protected List<RoomGameLocation> doInBackground(Void... Voids) {
            return gameLocationDao.getUnverifiedLocations();
        }

        @Override
        protected void onPostExecute(List<RoomGameLocation> roomGameLocations) {
            super.onPostExecute(roomGameLocations);
            delegate.onFinished(roomGameLocations);
        }
    }

    private interface AsyncResponse {
        void onFinished(List<RoomGameLocation> gameLocationModels);
    }

}
