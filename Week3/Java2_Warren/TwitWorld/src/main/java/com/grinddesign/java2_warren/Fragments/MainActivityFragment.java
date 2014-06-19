package com.grinddesign.java2_warren.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.grinddesign.java2_warren.twitworld.MainActivity;
import com.grinddesign.java2_warren.twitworld.R;

import org.json.JSONException;

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

        final ListView lv = (ListView) view.findViewById(R.id.tList);


        //create my header
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_cell, lv,
                false);
        lv.addHeaderView(header, null, false);

        lv.setOnItemClickListener(this);


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
            goldenObj = MainActivity.goldenArray.getString(position -1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("CRAZY", goldenObj);
        parentActivity.listItemSelected(goldenObj);
        //Log.i("Click It", parentActivity.toString());
    }




}
