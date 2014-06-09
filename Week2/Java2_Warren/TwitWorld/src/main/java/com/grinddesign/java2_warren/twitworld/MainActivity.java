package com.grinddesign.java2_warren.twitworld;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.grinddesign.java2_warren.classgroup.FilingCabinet;
import com.grinddesign.java2_warren.classgroup.TIntServ;
import com.grinddesign.test.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static ArrayList<String> dateLife;
    public static ArrayList<String> image;
    public static ArrayAdapter<String> mainListAdapter;

    Context thisHere = this;
    static FilingCabinet x_File;
    static String fileName = "string_from_twitter";
    final HandleMe tHand = new HandleMe(this);


    /**
     *
     * This is my onCreate where I create my adapters for my header and my listview.
     * I also call on my get data method to start my service.
     * My connection is also tested here.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test my connection
        Connection con = new Connection(this);
        con.connection();

        testArray = new ArrayList<String>();
        dateLife = new ArrayList<String>();
        image = new ArrayList<String>();

        final ListView lv = (ListView) findViewById(R.id.tList);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //create my header
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_cell, lv,
                false);
        lv.addHeaderView(header, null, false);




        //create adapter calling on the dynamic array from FeedMe Class // this will be dynamic data in week 3 from the API
        mainListAdapter = new custAdapter(thisHere, R.layout.item_cell, testArray);


        //load adapter into listview
        lv.setAdapter(mainListAdapter);

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("click me", "success");
                Intent detailPass = new Intent(thisHere, DetailActivity.class);

                detailPass.putExtra("file name", String.valueOf(fileName));
                startActivity(detailPass);
            }
        });

        //call method to start my class
        getData();

    }

    /**
     * This method creates my intent and starts my service.
     */
    public void getData() {
        Messenger serviceMessenger = new Messenger(tHand);
        Intent intent = new Intent(thisHere, TIntServ.class);
        intent.putExtra("messenger", serviceMessenger);
        startService(intent);


    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent detailPass = new Intent(thisHere, DetailActivity.class);

            detailPass.putExtra("file name", String.valueOf(fileName));
            startActivity(detailPass);
        }
    };



    /**
     * This is my inner class where I run my handler to handle my data that is returned from the file.
     * I then pass it to the reader which pulls the data from my file.
     * I turn the string to a JSONArray and Pars it and assign variables.
     */
    private static class HandleMe extends Handler {

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
                    x_File = FilingCabinet.getInstance();
                    Log.i("MAIN FILE NAME", msg.obj.toString());
                    Log.i("ENTER UPDATE", fileName);
                    Log.i("READ CONTEXT", actOnIt.toString());

                    try {
                        JSONArray readThatSucker = null;
                        try {
                            readThatSucker = new JSONArray(x_File.readingIt(actOnIt, fileName));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (readThatSucker != null) {
                            Log.i("CRAZY", "arrayTime" + readThatSucker.toString());
                            updateListData(readThatSucker);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("READ FILE", fileName);
                }

            }

        }
        public void updateListData(JSONArray readIt) {
            try {
                MainActivity actOnIt = activityPass.get();
                x_File = FilingCabinet.getInstance();
                for (int t=0; t<readIt.length(); t++) {
                    //reset stringbuilder each time
                    StringBuilder sb = new StringBuilder();
                    StringBuilder dtr = new StringBuilder();
                    StringBuilder url = new StringBuilder();
                    Log.i("test", "enter a ray");

                    //grab object at point in array that you have cycled to
                    JSONObject tweetObject = readIt.getJSONObject(t);
                    //Log.i("test", "enter a ray2");
                    try {
                        //grab the data you want to use

                        sb.append(tweetObject.getString("text"));
                        sb.append("\n");
                        url.append(tweetObject.getJSONObject("user").getString("profile_image_url"));


                        dtr.append(tweetObject.getString("created_at"));
                        dtr.append(
                                "\n");

                        Log.i("feed bs", sb.toString());
                        Log.i("date bs", sb.toString());
                        Log.i("image bs", sb.toString());
                        //give it some space with a break

                        Log.i("test", "enter a ray3");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sb.append("any random text");
                    }
                    //load the object pulled into a string
                    String posting = sb.toString();
                    String pDate = dtr.toString();
                    String urlStr = url.toString();

                    //assign variables
                    image.add(urlStr);
                    Log.i("test", image.toString());
                    //assign it to the array for the list adapter
                    testArray.add(posting);
                    dateLife.add(pDate);
                    Log.i("test", "enter a ray text");


                    //Log.d("this is my array", "arr45: " + MainActivity.image.toString());
                }
                //reset list adapter and force reload on listview
                actOnIt.mainListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("this is a JSON error", e.getMessage());
                e.printStackTrace();
            }



        }

    }




}
