package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.twitworld
 * <p/>
 * File:    ${File_Name}
 * <p/>
 * Purpose: ${Comments_Here}
 */
public class StarActivity extends Activity {
    public static ArrayList<String> starGrabber;
    public static ArrayAdapter<String> mainListAdapter;
    Context context;
    //static FilingCabinet s_File;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_starry);


        starGrabber = new ArrayList<String>();

        final ListView lv = (ListView) findViewById(R.id.tList);

        mainListAdapter = new custAdapter(context, R.layout.item_cell, starGrabber);


        //load adapter into listview
        lv.setAdapter(mainListAdapter);




    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
