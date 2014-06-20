package com.grinddesign.java2_warren.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.grinddesign.java2_warren.twitworld.MainActivity;
import com.grinddesign.java2_warren.twitworld.R;
import com.grinddesign.java2_warren.twitworld.custAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.Fragments
 * <p/>
 * File:    MainActivityFragment.java
 * <p/>
 * Purpose: This is my detail fragment that handles all my UI loading and behaviors
 */


public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static JSONArray goldenArray = new JSONArray();
    public static ArrayList<String> testArray;
    public static ArrayList<String> dateLife;
    public static ArrayList<String> image;
    public static ArrayList<String> twitId;
    public static ArrayAdapter<String> mainListAdapter;
    Context context;



    /**
     * This is the the interface for the main fragment
     */
    public interface onListClicked {

        void listItemSelected(String str);

    }

    /**
     * this is the private declaration for the main fragment
     */
    private onListClicked parentActivity;


    /**
     * the on Attach handeles making the methods accessible in any activity using the fragment
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onListClicked) {
            Log.i("Click It", "attached");
            this.context = activity;
            parentActivity = (onListClicked) activity;
            Log.i("Click It", parentActivity.toString());
        }
        else {
            throw new ClassCastException(activity.toString() + "must implement method" );
        }
    }

    /**
     * This is where we load the main activity UI in the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        testArray = new ArrayList<String>();
        dateLife = new ArrayList<String>();
        image = new ArrayList<String>();
        twitId = new ArrayList<String>();

        final ListView lv = (ListView) view.findViewById(R.id.tList);


        //create my header
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_cell, lv,
                false);
        lv.addHeaderView(header, null, false);

        lv.setOnItemClickListener(this);

        mainListAdapter = new custAdapter(context, R.layout.item_cell, testArray);


        //load adapter into listview
        lv.setAdapter(mainListAdapter);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);





        return view;
    }


    /**
     * This method handle the immediate reaction when a list item is clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Log.i("Click It", "Baby");
        String goldenObj = null;
        try {
            goldenObj = goldenArray.getString(position -1);
            Log.i("MADMAN", goldenObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("CRAZY", goldenObj);
        parentActivity.listItemSelected(goldenObj);
        //Log.i("Click It", parentActivity.toString());
    }



    /**
     * This Method handles the auto saving of the current instance to handle device stops/reloads so as not to reload data from web everytime
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivityFragment fraggle = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.fragment);
        outState.putString("message", goldenArray.toString());
        MainActivity.mainLove = outState;
        Log.i("RETAINER", String.valueOf(MainActivity.broken));
        fraggle.setRetainInstance(true);
    }


}




