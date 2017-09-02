package com.example.shenawynkov.jopfinder.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Shenawynkov on 8/25/2017.
 */

@IgnoreExtraProperties
public class User {
   final    public static int Empolyee=1;
    final    public static int Empolyer=0;

    public String username;
    public String email;
    public String phone;
    public int type;
    public String cv;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String phone,int type,Uri cv) {
        this.username = username;
        this.email = email;
        this.phone=phone;
        this.type=type;
        this.cv=cv.toString();
    }

}