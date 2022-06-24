package com.example.arcadefinder.Models;

import android.util.Log;
import android.widget.Toast;

import com.example.arcadefinder.RegisterActivity;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class ProfileModel {

    private String username;
    private ParseFile profileImage;
    private String profileImagePath;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ParseFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ParseFile profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

}
