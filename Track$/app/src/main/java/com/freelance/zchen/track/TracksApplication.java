package com.freelance.zchen.track;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class TracksApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Purchases.class);

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "L239QoxOAQHJEhoYRa0qEZUHAEVqD3nTL12ivdne", "pugVQh54xL3OxlpXM7oB4yizShft4C7hwTiLlsBh");
        
    }

}
