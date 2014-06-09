package com.grinddesign.java2_warren.twitworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    Context grabbinIt = this;
    View view;
    JSONObject breakDown = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        Intent intent = getIntent();
        if (null != intent) {
            String stringData= intent.getStringExtra("file name");
            Log.i("Passed", stringData);

            try {
                Log.i("WTF1", "enter a try");
                breakDown = new JSONObject(stringData);
                Log.i("WTF1", breakDown.toString());
                String url = breakDown.getJSONObject("user").getString("profile_image_url");
                String f = breakDown.getString("text");
                String d = breakDown.getString("created_at");
                String rt = breakDown.getString("retweet_count");
                String ti = breakDown.getString("id");
                String name = breakDown.getJSONObject("user").getString("name");
                String loc = breakDown.getJSONObject("user").getString("location");


                Log.i("WTF1", url);

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
                TextView locale = (TextView) findViewById(R.id.favCnt);
                locale.setText(name + " is from " + loc);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
