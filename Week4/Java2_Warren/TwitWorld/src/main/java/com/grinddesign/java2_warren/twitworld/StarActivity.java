package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.grinddesign.java2_warren.classgroup.FilingCabinet;

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
    static FilingCabinet s_File;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setContentView(R.layout.activity_starry);

        View view = inflater.inflate(R.layout.activity_starry, container, false);

        starGrabber = new ArrayList<String>();

        final ListView lv = (ListView) view.findViewById(R.id.tList);

        mainListAdapter = new custAdapter(context, R.layout.item_cell, starGrabber);


        //load adapter into listview
        lv.setAdapter(mainListAdapter);

        lv.setTextFilterEnabled(true);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        return view;
    }
}
