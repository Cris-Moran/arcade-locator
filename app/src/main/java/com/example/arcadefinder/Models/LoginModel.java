package com.example.arcadefinder.Models;

import com.parse.ParseUser;

public class LoginModel {
    ParseUser user;

    public ParseUser getUser() {
        return user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }
}
