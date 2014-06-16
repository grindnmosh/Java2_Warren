package com.grinddesign.java2_warren.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grinddesign.java2_warren.twitworld.R;

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
public class DetailActivityFragment extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);




        return view;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Method to listen rating bar and return rating when submitted back to the main application view
     */

}
