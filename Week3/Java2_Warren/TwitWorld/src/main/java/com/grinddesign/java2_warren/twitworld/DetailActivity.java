package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

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
public class DetailActivity extends Activity implements DetailActivityFragment.grind, DetailActivityFragment.face {
    private TextView txtRatingValue;

    String result = null;
    Button sub;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_frag);
        DetailActivityFragment fragment = (DetailActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentDetail);


        //check to see if there is a saved instance
        //if there is a saved instance
        if (savedInstanceState != null) {
            //grab saved instance data
            String reloadString = savedInstanceState.getString("message");


            //check orientation 1 = Portrait 2 = Landscape
            Log.i("ORIENTATION IS", String.valueOf(getResources().getConfiguration().orientation));
            //if landscape load this
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                fragment.loadItUp(reloadString);
            }
            //if portrait load this
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                fragment.loadItUp(reloadString);
            }
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

                    fragment.loadItUp(stringData);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    fragment.loadItUp(stringData);
                }

            }
        }





        addListenerOnRatingBar();

    }







    /**
     * Method to listen rating bar and return rating when submitted back to the main application view
     */
    public void addListenerOnRatingBar() {

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rateFeed);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //listens to rating change
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                //displays rating in text to right of the button
                txtRatingValue.setText(String.valueOf(rating));

                //declare rating into a string
                result = String.valueOf(rating);
                sub = (Button) findViewById(R.id.sub);
                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //pass data back
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",result);
                        setResult(RESULT_OK,returnIntent);
                        finish();

                    }
                });

            }
        });
    }

    @Override
    public void faceClicked() {
        Log.i("FACEOFF", "FACEOFF");
        Uri webpage = Uri.parse("https://www.facebook.com/GrindDesign");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    @Override
    public void grindClicked() {
        Uri webpage = Uri.parse("http://www.grind-design.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }
}
