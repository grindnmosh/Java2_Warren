package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.twitworld
 * <p/>
 * File:    DetailActivity.java
 * <p/>
 * Purpose: This will be where I create the code for the passed in details from my MainActivity.java and present the details and a rating chart  for each post.
 */
public class DetailActivity extends Activity {
    Context grabbinIt = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        Intent i = getIntent();
        // getting attached intent data
        String fileName = i.getStringExtra("file name");
        // displaying selected product name

    }
}
