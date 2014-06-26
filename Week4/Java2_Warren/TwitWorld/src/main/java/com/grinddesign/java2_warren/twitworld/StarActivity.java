package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    String[] starGrabber;
    ArrayAdapter<String> mainPistAdapter;
    Context context;
    //static FilingCabinet s_File;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_starry);

        context = this;

        starGrabber = getResources().getStringArray(R.array.choices_array);

        final ListView lv = (ListView) findViewById(R.id.starlist);

        mainPistAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,  starGrabber);

        //load adapter into listview
        lv.setAdapter(mainPistAdapter);




    }


}
