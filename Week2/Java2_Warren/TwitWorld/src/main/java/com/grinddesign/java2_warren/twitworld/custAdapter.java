package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Author:  Robert Warren
 *
 * Project:  Java2_Warren
 *
 * Package: com.grinddesign.java2_warren.twitworld
 *
 * File:    custAdapter.java
 *
 * Purpose: This is my custom adapter code that assigns the data to each field.
 */
public class custAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> arrayLister  = MainActivity.testArray;
    public custAdapter(Context context, int resource, ArrayList<String> arrayLister) {

        super(context, resource, arrayLister);
        Log.i("cust", "adapter1");
        this.context = context;
        this.arrayLister = arrayLister;
    }

    /**
     * This is my method to create the cells for the custom adapter
     * I use the Smart ImageView Library to handle my images.
     * I assign to 3 different fields
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("cust", "adapter");

        String i = MainActivity.image.get(position);
        String s = arrayLister.get(position);
        String d = MainActivity.dateLife.get(position);
        String id = MainActivity.twitId.get(position);



        //this is my code to inflate the custom layout
        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.item_cell, null);

        //this uses the smart imageview library to handle image urls
        SmartImageView myImage = (SmartImageView) view.findViewById(R.id.my_image);
        myImage.setImageUrl(i);

        //this is to assign the post to the proper field
        TextView tvMain = (TextView) view.findViewById(R.id.post);
        tvMain.setText(s);

        //this is to assign the date to the proper field
        TextView tvDate  = (TextView) view.findViewById(R.id.daters);
        tvDate.setText(d);

        TextView tvId  = (TextView) view.findViewById(R.id.idView);
        tvId.setText("Twit Id: " + id);




        Log.i("cust", "ready to return");
        return view;
    }
}
