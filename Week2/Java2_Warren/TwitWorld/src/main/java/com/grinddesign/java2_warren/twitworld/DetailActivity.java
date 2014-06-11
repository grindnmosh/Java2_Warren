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
 * Purpose: This will be where I create the code for the passed in details from my MainActivity.java and present the details and a rating chart  for each post.
 */
public class DetailActivity extends Activity {
    private TextView txtRatingValue;
    JSONObject breakDown = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        if (savedInstanceState != null) {
            String reloadString = savedInstanceState.getString("message");

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.activity_detailland);
                loadItUp(reloadString);
            }
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setContentView(R.layout.activity_detail);
                loadItUp(reloadString);
            }
        }
        else {
            Intent intent = getIntent();
            if (null != intent) {
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



        Button webbie = (Button) findViewById(R.id.webGD);
        Button faceIt = (Button) findViewById(R.id.fbGD);

        webbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("http://www.grind-design.com");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }
        });

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

    public void loadItUp(String string) {
        try {
            Log.i("WTF1", "enter a try");
            breakDown = new JSONObject(string);
            Log.i("WTF1", breakDown.toString());
            String url = breakDown.getJSONObject("user").getString("profile_image_url");
            String f = breakDown.getString("text");
            String d = breakDown.getString("created_at");
            String rt = breakDown.getString("retweet_count");
            String ti = breakDown.getString("id");
            String name = breakDown.getJSONObject("user").getString("name");
            String loc = breakDown.getJSONObject("user").getString("location");




            SmartImageView myImage = (SmartImageView) findViewById(R.id.my_image);
            myImage.setImageUrl(url);
            TextView feeder = (TextView) findViewById(R.id.feed);
            feeder.setText(f);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", breakDown.toString());
    }


    public void addListenerOnRatingBar() {

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rateFeed);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));
                final String result = String.valueOf(rating);
                Button sub = (Button) findViewById(R.id.sub);
                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
