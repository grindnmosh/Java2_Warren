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
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.twitworld
 * <p/>
 * File:    ${File_Name}
 * <p/>
 * Purpose: ${Comments_Here}
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("cust", "adapter");

        String i = MainActivity.image.get(position);
        String s = arrayLister.get(position);
        String d = MainActivity.dateLife.get(position);


        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.item_cell, null);

        SmartImageView myImage = (SmartImageView) view.findViewById(R.id.my_image);
        myImage.setImageUrl(i);

        TextView tvMain = (TextView) view.findViewById(R.id.post);
        tvMain.setText(s);

        TextView tvDate  = (TextView) view.findViewById(R.id.daters);
        tvDate.setText(d);


        Log.i("cust", "ready to return");
        return view;
    }
}
