package com.example.arcadefinder.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.GameInfoModel;
import com.parse.ParseFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GameInfoRepo {
    public MutableLiveData<GameInfoModel> getGameInfoModel(ParseGameLocation parseGameLocation) {
        String title = parseGameLocation.getTitle();
        ParseFile image = parseGameLocation.getImage();
        String address = parseGameLocation.getAddress();
        String userDesc = parseGameLocation.getDescription();
        String encodeTitle = "";
        try {
            encodeTitle = URLEncoder.encode(title, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        GameInfoModel gameInfoModel = new GameInfoModel();
        gameInfoModel.setTitle(title);
        gameInfoModel.setImage(image);
        gameInfoModel.setAddress(address);
        gameInfoModel.setUserDesc(userDesc);
        gameInfoModel.setEncodeTitle(encodeTitle);

        MutableLiveData<GameInfoModel> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(gameInfoModel);
        return mutableLiveData;
    }
}
