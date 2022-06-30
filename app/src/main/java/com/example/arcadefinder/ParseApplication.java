package com.example.arcadefinder;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(GameLocation.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("imBrAnlrYaBogOvZXBVJuwk3Q8r90F4Ei8SN7gyt")
                .clientKey("qHuH5yoj6hZq8mP0fDaZPhyqxBIwoxd8ZQyLf748")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
