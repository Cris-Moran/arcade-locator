package com.example.arcadefinder.Models;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class UploadModel {

    private ParseGeoPoint coordinates;
    private String locationName;
    private String address;
    private String title;
    private String description;
    private ParseFile image;
    private boolean isVerified;
    private ParseUser author;


    public ParseGeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ParseGeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParseFile getImage() {
        return image;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

    public ParseUser getAuthor() {
        return author;
    }

    public void setAuthor(ParseUser author) {
        this.author = author;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean verified) {
        this.isVerified = verified;
    }

}
