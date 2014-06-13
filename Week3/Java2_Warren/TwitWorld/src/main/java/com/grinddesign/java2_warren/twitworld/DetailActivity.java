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

import com.loopj.android.image.SmartImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
public class DetailActivity extends Activity {
    private TextView txtRatingValue;
    JSONObject breakDown = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        //check to see if there is a saved instance
        //if there is a saved instance
        if (savedInstanceState != null) {
            //grab saved instance data
            String reloadString = savedInstanceState.getString("message");

            //check orientation 1 = Portrait 2 = Landscape
            Log.i("ORIENTATION IS", String.valueOf(getResources().getConfiguration().orientation));
            //if landscape load this
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.activity_detailland);
                loadItUp(reloadString);
            }
            //if portrait load this
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setContentView(R.layout.activity_detail);
                loadItUp(reloadString);
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

                Log.i("Passed", stringData);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    setContentView(R.layout.activity_detailland);
                    loadItUp(stringData);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setContentView(R.layout.activity_detail);
                    loadItUp(stringData);
                }

            }
        }


        /**
         * These are the onclick methods used to call each of the 2 implicit intents.
         */
        Button webbie = (Button) findViewById(R.id.webGD);
        Button faceIt = (Button) findViewById(R.id.fbGD);

        //implicit intent #1
        webbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("http://www.grind-design.com");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }
        });

        //implicit intent #2
        faceIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://www.facebook.com/GrindDesign");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }
        });

        addListenerOnRatingBar();









    }

    /**
     * Method to parse passed in data when called on
     */
    public void loadItUp(String string) {
        try {
            Log.i("WTF1", "enter a try");
            breakDown = new JSONObject(string);
            Log.i("BREAKDOWN", breakDown.toString());
            String url = breakDown.getJSONObject("user").getString("profile_image_url");
            String f = breakDown.getString("text");
            String d = breakDown.getString("created_at");
            String rt = breakDown.getString("retweet_count");
            String ti = breakDown.getString("id");
            String name = breakDown.getJSONObject("user").getString("name");
            String loc = breakDown.getJSONObject("user").getString("location");
            Log.i("did it get here", loc);
            String exUrl = breakDown.getJSONObject("entities").getJSONArray("urls").getJSONObject(0).getString("expanded_url");
            Log.i("EXPANDED URL", exUrl);




            SmartImageView myImage = (SmartImageView) findViewById(R.id.my_image);
            myImage.setImageUrl(url);
            TextView feeder = (TextView) findViewById(R.id.feed);
            feeder.setText(f);
            TextView expandedUrl = (TextView) findViewById(R.id.url);
            expandedUrl.setText(exUrl);
            TextView dately = (TextView) findViewById(R.id.detDate);
            dately.setText(d);
            TextView retweet = (TextView) findViewById(R.id.retwtCnt);
            retweet.setText("Retweeted " + rt + " times");
            TextView tId = (TextView) findViewById(R.id.twitId);
            tId.setText("post ID = " + ti);
            TextView locale = (TextView) findViewById(R.id.usrInfo);
            locale.setText(name + " is from " + loc);



        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    /**
     * This Method handles the auto saving of the current instance to handle device stops/reloads so as not to reload data from web everytime
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", breakDown.toString());
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
                final String result = String.valueOf(rating);
                Button sub = (Button) findViewById(R.id.sub);
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

}
