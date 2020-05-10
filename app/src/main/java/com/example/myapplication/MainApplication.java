package com.example.myapplication;

import android.app.Application;

public class MainApplication extends Application {
    public static MainApplication appInstance;

    public static MainApplication getInstance() {
        if (appInstance == null) {
            appInstance = new MainApplication();
        }
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

    }
    public ApiMovies getApiMovies() {
        return ServiceGenerator.createService(ApiMovies.class);
    }
}
