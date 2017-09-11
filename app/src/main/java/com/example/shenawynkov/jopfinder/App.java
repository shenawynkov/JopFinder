package com.example.shenawynkov.jopfinder;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shenawynkov on 9/11/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
