package com.grinddesign.java2_warren.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.grinddesign.java2_warren.classgroup.FilingCabinet;
import com.grinddesign.java2_warren.twitworld.MainActivity;
import com.grinddesign.java2_warren.twitworld.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.Fragments
 * <p/>
 * File:    DetailActivityFragment.java
 * <p/>
 * Purpose: This is my detail fragment that handles all my UI loading and behaviors
 */
public class DetailActivityFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {

    JSONObject breakDown = null;
    TextView feeder;
    TextView expandedUrl;
    TextView dately;
    TextView retweet;
    TextView tId;;
    Button grind;
    Button face;
    RatingBar rb;
    TextView txtRatingValue;
    String result = null;
    Button sub;
    String fileName = "starry_night";
    FilingCabinet s_File;
    Context context;


    /**
     * These are the interfaces for the detail fragment
     */
    public interface grind {
        void grindClicked();
    }

    public interface face {
        void faceClicked();
    }

    public interface beRated {
        void starryEyes(String id);
    }

    /**
     * These are the private declarations for the interfaces
     */
    private grind parentActivity1;
    private face parentActivity2;
    private  beRated parentActivity3;

    /**
     * This is where we load the main activity UI in the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);


        rb = (RatingBar) view.findViewById(R.id.rateFeed);
        feeder = (TextView) view.findViewById(R.id.feed);
        expandedUrl = (TextView) view.findViewById(R.id.url);
        dately = (TextView) view.findViewById(R.id.detDate);
        retweet = (TextView) view.findViewById(R.id.retwtCnt);
        tId = (TextView) view.findViewById(R.id.twitId);
        sub = (Button) view.findViewById(R.id.sub);
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


        if ((savedInstanceState != null) && (savedInstanceState.getSerializable("starttime") != null)) {
            String reloadString = (String) savedInstanceState.getSerializable("starttime");

            Log.i("RELOAD", reloadString);
        }



        return view;


    }

    /**
     * this is the method that loads the data into the various fields
     */
    public void loadItUp(String string) {
        try {
            Log.i("WTF1", "enter a try");
            breakDown = new JSONObject(string);
            Log.i("BREAKDOWN", breakDown.toString());
            String f = breakDown.getString("text");
            String d = breakDown.getString("created_at");
            String rt = breakDown.getString("retweet_count");
            String ti = breakDown.getString("id");
            String exUrl = breakDown.getJSONObject("entities").getJSONArray("urls").getJSONObject(0).getString("url");
            Log.i("EXPANDED URL", exUrl);





            feeder.setText(f);
            expandedUrl.setText("URL: " + exUrl);
            dately.setText("Date: " + d);
            retweet.setText("Retweeted " + rt + " times");
            tId.setText("post ID = " + ti);




        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    /**
     * This Method handles the auto saving of the current instance to handle device stops/reloads so as not to reload data from web everytime
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        DetailActivityFragment fragment = (DetailActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentDetail);
        if (breakDown != null) {
            outState.putString("detail_message", breakDown.toString());
            Log.i("RETAIN", outState.toString());
            MainActivity.broken = outState;
            Log.i("RETAINER", String.valueOf(MainActivity.broken));
            fragment.setRetainInstance(true);
        }

    }

    /**
     * the on Attach handeles making the methods accessible in any activity using the fragment
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        parentActivity1 = (grind) activity;
        parentActivity2 = (face) activity;
        parentActivity3 = (beRated) activity;
    }

    /**
     * This method handles the star ratings from the detail fragment
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
        //displays rating in text to right of the button
        txtRatingValue.setText(String.valueOf(rating));

        //declare rating into a string
        result = String.valueOf(rating);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String twitId = (String) tId.getText();
                Log.i("BreakingOUT", twitId);
                MainActivity ma = new MainActivity();
                ma.saveItAll(twitId, result);
                parentActivity3.starryEyes(result);

            }
        });
    }
}
