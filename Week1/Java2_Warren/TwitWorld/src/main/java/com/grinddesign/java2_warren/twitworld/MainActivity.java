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

import com.grinddesign.java2_warren.classgroup.TParse;
import com.grinddesign.java2_warren.classgroup.TServ;

import org.json.JSONArray;

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
    StringBuilder rockTheT;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedArray = new JSONArray();
        testArray = new ArrayList<String>();
        final ListView lv = (ListView) findViewById(R.id.tList);
        TParse tp = new TParse(this);
        tp.twitThis();
        Log.i("test", testArray.toString());

        //startService(new Intent(thisHere, TServ.class));


        //create adapter calling on the dynamic array from FeedMe Class // this will be dynamic data in week 3 from the API
        mainListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testArray);
        //postAdapter = new CellAdapter(twitCon, R.layout.item_cell, testArray);


        //load adapter into listview
        lv.setAdapter(mainListAdapter);

        Handler tHandle = new Handler() {
            public void handler(Message msg) {
                String responder = null;

                if (msg.obj != null) {
                    responder = (String) msg.obj;
                }
                rockTheT.append(responder);


            }
        };



        Messenger serviceMessenger = new Messenger(tHandle);
        Intent intent = new Intent(thisHere, TServ.class);
        intent.putExtra("messenger", serviceMessenger);
        startService(intent);

    }







}
