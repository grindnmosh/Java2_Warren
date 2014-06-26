package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.twitworld
 * <p/>
 * File:    AboutMeActivity.java
 * <p/>
 * Purpose: This file just loads a static xml using string resources
 */
public class AboutMeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAPPED OUT", "reached About Me");
        setContentView(R.layout.about_me);


    }
}
