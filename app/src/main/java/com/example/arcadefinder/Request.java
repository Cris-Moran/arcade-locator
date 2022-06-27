package com.example.arcadefinder;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Request")
public class Request extends ParseObject {
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ADDRESS = "location";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_VERIFIED = "verified";

    // TODO: Should this be my repo? Or should the repo query this

    public Request() {
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_ADDRESS);
    }

    public void setLocation(ParseGeoPoint parseGeoPoint) {
        put(KEY_ADDRESS, parseGeoPoint);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String string) {
        put(KEY_TITLE, string);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String string) {
        put(KEY_DESCRIPTION, string);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser parseUser) {
        put(KEY_AUTHOR, parseUser);
    }

    public void setIsVerified(boolean bool) {
        put(KEY_VERIFIED, bool);
    }

    public boolean getIsVerified() {
        return getBoolean(KEY_VERIFIED);
    }

}
