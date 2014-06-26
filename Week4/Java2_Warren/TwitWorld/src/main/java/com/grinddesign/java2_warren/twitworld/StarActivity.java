package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    ArrayList<String> starGrabber;
    public static ArrayAdapter<String> mainPistAdapter;
    Context context;
    SharedPreferences sharedpreferences;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor edit;
    //static FilingCabinet s_File;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_starry);

        context = this;

        starGrabber = new ArrayList<String>();

        sharedpreferences = getSharedPreferences("starr", Context.MODE_PRIVATE);
        Log.i("TESTPREFS", sharedpreferences.toString());




        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        edit = preferences.edit();
        if (preferences.getString("starred", "").isEmpty()) {
            Log.i("PREFTEST", "ENTERED");

        }

        else if (!preferences.getString("username", "").isEmpty()) {
            Log.i("PREFTEST", "ENTERED");
            Log.i("CRACKALACK", preferences.getString("starred", ""));
            try {
                JSONArray loadMe = new JSONArray(preferences.getString("starred", ""));
                getData(loadMe);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        final ListView lv = (ListView) findViewById(R.id.starlist);

        mainPistAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,  starGrabber);

        //load adapter into listview
        lv.setAdapter(mainPistAdapter);




    }

    private void getData(JSONArray bringIt) {
        try {
            for (int t=0; t<bringIt.length(); t++) {
                //reset stringbuilder each time
                StringBuilder starLight = new StringBuilder();

                Log.i("test", "enter a ray");

                //grab object at point in array that you have cycled to
                JSONObject tweetObject = bringIt.getJSONObject(t);
                //Log.i("test", "enter a ray2");
                try {
                    //grab the data you want to use

                    starLight.append(tweetObject.getString("stars"));

                    Log.i("feed bs", starLight.toString());


                    Log.i("test", "enter a ray3");
                } catch (JSONException e) {
                    e.printStackTrace();
                    starLight.append("any random text");
                }
                //load the object pulled into a string
                String starry = starLight.toString();


                //assign variables
                starGrabber.add(starry);
                Log.i("test", starGrabber.toString());


                //Log.d("this is my array", "arr45: " + MainActivity.image.toString());
            }
            //reset list adapter and force reload on listview
            //mainPistAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
