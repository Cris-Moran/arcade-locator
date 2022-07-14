package com.example.arcadefinder;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Location")
public class GameLocation extends ParseObject {
    public static final String KEY_COORDINATES = "coordinates";
    public static final String KEY_LOCATION_NAME = "locationName";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_VERIFIED = "isVerified";
    public static final String KEY_ID = "objectId";


    public GameLocation() {
    }

    public ParseGeoPoint getCoordinates() {
        return getParseGeoPoint(KEY_COORDINATES);
    }

    public void setCoordinates(ParseGeoPoint parseGeoPoint) {
        put(KEY_COORDINATES, parseGeoPoint);
    }

    public String getLocationName() {
        return getString(KEY_LOCATION_NAME);
    }

    public void setLocationName(String locationName) {
        put(KEY_LOCATION_NAME, locationName);
    }

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author) {
        put(KEY_AUTHOR, author);
    }

    public boolean getIsVerified() {
        return getBoolean(KEY_VERIFIED);
    }

    public void setIsVerified(boolean verified) {
        put(KEY_VERIFIED, verified);
    }


}
