package com.example.arcadefinder.Models;

import com.parse.ParseFile;


public class ProfileModel {

    private String username;
    private ParseFile profileImage;
    private String profileImagePath;
    private boolean isAdmin;

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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
