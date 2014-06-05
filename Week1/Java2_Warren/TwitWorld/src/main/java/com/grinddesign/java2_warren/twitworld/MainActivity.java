package com.grinddesign.java2_warren.twitworld;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.grinddesign.java2_warren.classgroup.FilingCabinet;
import com.grinddesign.java2_warren.classgroup.TIntServ;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Author:  Robert Warren
 *
 * Project:  Java2_Warren
 *
 * Package: com.grinddesign.java2_warren.twitWorld
 *
 * File:    MainActivity.Java
 *
 * Purpose: This is where all the action happens to actually present the application on the device screen and to direct the traffic of what needs to run and when.
 */


public class MainActivity extends Activity {


    public static ArrayList<String> testArray;
    public static JSONArray feedArray;
    public static ArrayAdapter<String> mainListAdapter;
    Context thisHere = this;
    FilingCabinet x_File;
    String fileName = "string_from_twitter";
    final HandleMe tHand = new HandleMe(this);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView lv = (ListView) findViewById(R.id.tList);

        getData();


        //create adapter calling on the dynamic array from FeedMe Class // this will be dynamic data in week 3 from the API
        //mainListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testArray);
        //postAdapter = new CellAdapter(twitCon, R.layout.item_cell, testArray);


        //load adapter into listview
        //lv.setAdapter(mainListAdapter);

    }
    public void getData() {
        Messenger serviceMessenger = new Messenger(tHand);
        Intent intent = new Intent(thisHere, TIntServ.class);
        intent.putExtra("messenger", serviceMessenger);
        startService(intent);
    }

    private class HandleMe extends Handler {

        private final WeakReference<MainActivity> activityPass;

        public HandleMe(MainActivity activity) {
            activityPass = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i("TEST ME", "handle me");
            MainActivity actOnIt = activityPass.get();
            if (actOnIt != null) {

                Object retObj = msg.obj;
                if (retObj != null) {
                    Log.i("MAIN FILE NAME", msg.obj.toString());
                    updateListData();
                }
            }


        }
    }



    public void updateListData() {

        Log.i("ENTER UPDATE", fileName);
        Log.i("READ CONTEXT", thisHere.toString());
        String stringToDisplay = x_File.readingIt(thisHere, fileName);
        Log.i("READ FILE", fileName);
        //Toast.makeText(getBaseContext(), "READ -> " + displayTest, Toast.LENGTH_SHORT).show();
    }
}
