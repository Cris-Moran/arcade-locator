package com.example.arcadefinder.Models;

import com.parse.ParseFile;

public class GameInfoModel {
    private String title;
    private ParseFile image;
    private String address;
    private String locationName;
    private String userDesc;
    private String encodeTitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ParseFile getImage() {
        return image;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getEncodeTitle() {
        return encodeTitle;
    }

    public void setEncodeTitle(String encodeTitle) {
        this.encodeTitle = encodeTitle;
    }
}
