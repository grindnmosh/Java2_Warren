package com.grinddesign.java2_warren.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.grinddesign.java2_warren.twitworld.R;
import com.loopj.android.image.SmartImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
public class DetailActivityFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {

    JSONObject breakDown = null;
    SmartImageView myImage;
    TextView feeder;
    TextView expandedUrl;
    TextView dately;
    TextView retweet;
    TextView tId;
    TextView locale;
    Button grind;
    Button face;
    TextView txtRatingValue;
    RatingBar rb;




    public interface grind {
        void grindClicked();
    }

    public interface face {
        void faceClicked();
    }

    public interface beRated {
        void starryEyes(float rating);
    }

    private grind parentActivity1;
    private face parentActivity2;
    private  beRated parentActivity3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);

        rb = (RatingBar) view.findViewById(R.id.rateFeed);
        myImage = (SmartImageView) view.findViewById(R.id.my_image);
        feeder = (TextView) view.findViewById(R.id.feed);
        expandedUrl = (TextView) view.findViewById(R.id.url);
        dately = (TextView) view.findViewById(R.id.detDate);
        retweet = (TextView) view.findViewById(R.id.retwtCnt);
        tId = (TextView) view.findViewById(R.id.twitId);
        locale = (TextView) view.findViewById(R.id.usrInfo);
        txtRatingValue = (TextView) view.findViewById(R.id.txtRatingValue);
        grind = (Button) view.findViewById(R.id.webGD);
        grind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity1.grindClicked();
            }
        });
        face = (Button) view.findViewById(R.id.fbGD);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("WTFClick", "123");
                parentActivity2.faceClicked();
            }
        });

        rb.setOnRatingBarChangeListener(this);

        return view;


    }

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





            myImage.setImageUrl(url);
            feeder.setText(f);
            expandedUrl.setText(exUrl);
            dately.setText(d);
            retweet.setText("Retweeted " + rt + " times");
            tId.setText("post ID = " + ti);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        parentActivity1 = (grind) activity;
        parentActivity2 = (face) activity;
        parentActivity3 = (beRated) activity;
    }





    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        parentActivity3.starryEyes(rating);
    }



}