package com.example.gabim.godiva.util;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by gabim on 07/04/2017.
 */

public class GoDiva extends Application{
    @Override
    public void onCreate(){
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
