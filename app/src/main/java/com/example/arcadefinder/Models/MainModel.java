package com.example.arcadefinder.Models;

import com.parse.ParseUser;

public class MainModel {

    ParseUser user;
    boolean verifyStatus;

    public boolean getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(boolean verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public ParseUser getUser() {
        return user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }
}
