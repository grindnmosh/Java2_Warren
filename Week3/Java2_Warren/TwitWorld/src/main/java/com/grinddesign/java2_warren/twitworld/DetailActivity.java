package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.grinddesign.java2_warren.Fragments.DetailActivityFragment;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.twitworld
 * <p/>
 * File:    DetailActivity.java
 * <p/>
 * Purpose: This will be where I create the code for the passed in details from my MainActivity.java and present the details and a rating chart that returns data back to the main activity for each post.
 */
public class DetailActivity extends Activity implements DetailActivityFragment.grind, DetailActivityFragment.face, DetailActivityFragment.beRated {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_frag);
        DetailActivityFragment fragment = (DetailActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentDetail);



        //check to see if there is a saved instance
        //if there is a saved instance
        if (savedInstanceState != null) {
            //grab saved instance data



            //check orientation 1 = Portrait 2 = Landscape
            Log.i("ORIENTATION IS", String.valueOf(getResources().getConfiguration().orientation));
            //if landscape load this
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                finish();
            }

            //if portrait load this

        }
        //if no saved instance
        else {
            //grab passed intent
            Intent intent = getIntent();
            //if intent was passed then do this otherwise do nothing
            if (null != intent) {
                //grab string fom intent
                String stringData = intent.getStringExtra("file name");

                //Log.i("Passed", stringData);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    finish();
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    fragment.loadItUp(stringData);
                }

            }
        }

    }


    /**
     * This method handles the Facebook implicit intent from the detail fragment
     */
    @Override
    public void faceClicked() {
        Log.i("FACEOFF", "FACEOFF");
        Uri webpage = Uri.parse("https://www.facebook.com/GrindDesign");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    /**
     * This method handles the grinddesign.com implicit intent from the detail fragment
     */
    @Override
    public void grindClicked() {
        Uri webpage = Uri.parse("http://www.grind-design.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    /**
     * Method to listen rating bar and return rating when submitted back to the main application view
     */
    @Override
    public void starryEyes(String result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",result);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
