package com.grinddesign.java2_warren.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.grinddesign.java2_warren.twitworld.DetailActivity;
import com.grinddesign.java2_warren.twitworld.MainActivity;
import com.grinddesign.java2_warren.twitworld.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.Fragments
 * <p/>
 * File:    ${File_Name}
 * <p/>
 * Purpose: ${Comments_Here}
 */


public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {
    JSONArray goldenArray = new JSONArray();
    MainActivity thisHere = new MainActivity();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailPass = new Intent(thisHere, DetailActivity.class);

        String goldenObj = null;
        try {
            goldenObj = goldenArray.getString(position -1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = goldenObj;
        parentActivity.startResultActivity(str);
    }



    public interface onListClicked {
        void startResultActivity(String str);

    }

    private onListClicked parentActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onListClicked) {
            parentActivity = (onListClicked)activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        final ListView lv = (ListView) view.findViewById(R.id.tList);


        //create my header
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_cell, lv,
                false);
        lv.addHeaderView(header, null, false);


        return view;
    }
}
