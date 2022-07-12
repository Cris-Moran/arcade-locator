package com.example.arcadefinder.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.GameInfoModel;
import com.parse.ParseFile;

public class GameInfoRepo {
    public MutableLiveData<GameInfoModel> getGameInfoModel(GameLocation gameLocation) {
        GameInfoModel gameInfoModel = new GameInfoModel();
        String title = gameLocation.getTitle();
        ParseFile image = gameLocation.getImage();
        String address = gameLocation.getAddress();
        String userDesc = gameLocation.getDescription();
        gameInfoModel.setTitle(title);
        gameInfoModel.setImage(image);
        gameInfoModel.setAddress(address);
        gameInfoModel.setUserDesc(userDesc);
        MutableLiveData<GameInfoModel> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(gameInfoModel);
        return mutableLiveData;
    }
}
