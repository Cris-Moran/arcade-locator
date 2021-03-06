package com.example.arcadefinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.Models.GameInfoModel;
import com.example.arcadefinder.Repositories.GameInfoRepo;

public class GameInfoViewModel extends ViewModel {
    MutableLiveData<GameInfoModel> mutableLiveData;
    GameInfoRepo gameInfoRepo;

    public GameInfoViewModel() {
        this.gameInfoRepo = new GameInfoRepo();
    }

    public LiveData<GameInfoModel> getGameInfoModel(ParseGameLocation parseGameLocation) {
        if (mutableLiveData == null) {
            mutableLiveData = gameInfoRepo.getGameInfoModel(parseGameLocation);
        }
        return mutableLiveData;
    }
}
