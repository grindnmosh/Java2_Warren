package com.grinddesign.java2_warren.twitworld;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.grinddesign.java2_warren.Fragments.DetailActivityFragment;
import com.grinddesign.java2_warren.Fragments.MainActivityFragment;
import com.grinddesign.java2_warren.classgroup.FilingCabinet;
import com.grinddesign.java2_warren.classgroup.TIntServ;
import com.grinddesign.test.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Author:  Robert Warren
 *
 * Project:  Java2_Warren
 *
 * Package: com.grinddesign.java2_warren.twitWorld
 *
 * File:    MainActivity.Java
 *
 * Purpose: This is where all the action happens to actually present the application on the device screen and to direct the traffic of what needs to run and when.
 */

public class MainActivity extends Activity implements MainActivityFragment.onListClicked, DetailActivityFragment.grind, DetailActivityFragment.face, DetailActivityFragment.beRated  {


    public static ArrayList<String> testArray;
    public static ArrayList<String> dateLife;
    public static ArrayList<String> image;
    public static ArrayList<String> twitId;
    public static ArrayAdapter<String> mainListAdapter;
    public static JSONArray goldenArray = new JSONArray();
    Context thisHere = this;
    static FilingCabinet x_File;
    static String fileName = "string_from_twitter";
    final HandleMe tHand = new HandleMe(this);
    String result = null;


    /**
     *
     * This is my onCreate where I create my adapters for my header and my listview.
     * I also call on my get data method to start my service.
     * My connection is also tested here.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frag);
        DetailActivityFragment fragment = (DetailActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentDetail);

        testArray = new ArrayList<String>();
        dateLife = new ArrayList<String>();
        image = new ArrayList<String>();
        twitId = new ArrayList<String>();

        final ListView lv = (ListView) findViewById(R.id.tList);

        //create adapter calling on the dynamic array from FeedMe Class // this will be dynamic data in week 3 from the API
        mainListAdapter = new custAdapter(thisHere, R.layout.item_cell, testArray);


        //load adapter into listview
        lv.setAdapter(mainListAdapter);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);




        //check for saved instance
        //if there is a saved instance
        if( savedInstanceState != null ) {
            //data from saved instance
            String passerBy = savedInstanceState.getString("message");
            String reloadString = savedInstanceState.getString("detail_message");
            try {
                JSONArray readThatSucker = new JSONArray(passerBy);
                Log.i("readItAgain", readThatSucker.toString());
                tHand.updateListData(readThatSucker);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //if no saved instance
        else {
            //use conection to determine whether to load from file or refresh file
            ConnectivityManager cm = (ConnectivityManager) thisHere.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                //test my connection
                Connection con = new Connection(this);
                con.connection();

                //call method to start my class
                MainActivityFragment fraggleRock = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.fragment);
                getData();
            }
            else {
                File file = thisHere.getFileStreamPath(fileName);
                if (file.isFile()) {
                    //load previously saved data if it exists
                    //File file = new File(Environment.getExternalStorageDirectory() + fileName);
                    Message msg = Message.obtain();
                    msg.arg1 = Activity.RESULT_OK;
                    msg.obj = fileName;
                    HandleMe hm = new HandleMe(this);
                    hm.handleMessage(msg);
                    Toast.makeText(thisHere, "loaded file from previously saved file", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(thisHere, "No previous file to load. Please connect to a network", Toast.LENGTH_SHORT).show();
                }

            }
        }






    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", goldenArray.toString());
    }



    @Override
    public void listItemSelected(String str) {

        DetailActivityFragment fragment = (DetailActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentDetail);


            if (fragment != null && fragment.isInLayout()) {

                fragment.loadItUp(str);
            }
            else {

                startResultActivity(str);
            }


    }


    public void startResultActivity(String str) {
        //Log.i("Click It", str);
        Intent detailPass = new Intent(thisHere, DetailActivity.class);
        Log.i("Cray Cray", str);
        detailPass.putExtra("file name", str);
        Log.i("Cray Cray", detailPass.toString());
        startActivityForResult(detailPass, 1);
    }


    /**
     * This method creates my intent and starts my service.
     */
    public void getData() {
        Messenger serviceMessenger = new Messenger(tHand);
        Intent intent = new Intent(thisHere, TIntServ.class);
        intent.putExtra("messenger", serviceMessenger);
        startService(intent);


    }

    /**
     * This method gathers the information passed back about the tweet rating from the detail page
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("result");
                String title = "Robert's Tweet Rating by you";
                Log.i("RESULT", result);
                //create alert to display info passed back in the format of my choosing
                AlertDialog.Builder displayResult = new AlertDialog.Builder(this);
                displayResult.setTitle(title).setMessage("You Rated this tweet a " + result + " and we thank you for taking the time to rate our tweet").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                displayResult.create();
                displayResult.show();

            }
        }
    }





    /**
     * This Method handles the auto saving of the current instance to handle device stops/reloads so as not to reload data from web everytime
     */


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



    @Override
    public void starryEyes(String str) {

        String title = "Robert's Tweet Rating by you";
        AlertDialog.Builder displayResult = new AlertDialog.Builder(this);
        displayResult.setTitle(title).setMessage("You Rated this tweet a " + str + " and we thank you for taking the time to rate our tweet").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        displayResult.create();
        displayResult.show();
        //grabby(RESULT_OK, returnIntent);
    }

    protected void grabby(int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                String title = "Robert's Tweet Rating by you";
                AlertDialog.Builder displayResult = new AlertDialog.Builder(this);
                displayResult.setTitle(title).setMessage("You Rated this tweet a " + result + " and we thank you for taking the time to rate our tweet").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                displayResult.create();
                displayResult.show();
            }


    }


    /**
     * This is my inner class where I run my handler to handle my data that is returned from the file.
     * I then pass it to the reader which pulls the data from my file.
     * I turn the string to a JSONArray and Pars it and assign variables.
     */
    private static class HandleMe extends Handler {

        private final WeakReference<MainActivity> activityPass;

        public HandleMe(MainActivity activity) {
            activityPass = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i("TEST ME", "handle me");
            MainActivity actOnIt = activityPass.get();
            if (actOnIt != null) {

                Object retObj = msg.obj;
                if (retObj != null) {
                    x_File = FilingCabinet.getInstance();
                    Log.i("MAIN FILE NAME", msg.obj.toString());
                    Log.i("ENTER UPDATE", fileName);
                    //Log.i("READ CONTEXT", actOnIt.toString());

                    try {
                        JSONArray readThatSucker = null;
                        try {
                            readThatSucker = new JSONArray(x_File.readingIt(actOnIt, fileName));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (readThatSucker != null) {
                            Log.i("CRAZY", "arrayTime" + readThatSucker.toString());
                            updateListData(readThatSucker);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("READ FILE", fileName);
                }

            }

        }

        /**
         * Method to parse the data that is passed in
         */
        public void updateListData(JSONArray readIt) {
            try {
                MainActivity actOnIt = activityPass.get();
                actOnIt.goldenArray = readIt;
                x_File = FilingCabinet.getInstance();
                for (int t=0; t<readIt.length(); t++) {
                    //reset stringbuilder each time
                    StringBuilder sb = new StringBuilder();
                    StringBuilder dtr = new StringBuilder();
                    StringBuilder url = new StringBuilder();
                    StringBuilder tId = new StringBuilder();
                    Log.i("test", "enter a ray");

                    //grab object at point in array that you have cycled to
                    JSONObject tweetObject = readIt.getJSONObject(t);
                    //Log.i("test", "enter a ray2");
                    try {
                        //grab the data you want to use

                        sb.append(tweetObject.getString("text"));
                        sb.append("\n");
                        url.append(tweetObject.getJSONObject("user").getString("profile_image_url"));
                        dtr.append(tweetObject.getString("created_at"));
                        tId.append(tweetObject.getString("id"));

                        Log.i("feed bs", sb.toString());
                        Log.i("date bs", url.toString());
                        Log.i("image bs", dtr.toString());
                        Log.i("id bs", tId.toString());
                        //give it some space with a break

                        Log.i("test", "enter a ray3");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sb.append("any random text");
                    }
                    //load the object pulled into a string
                    String posting = sb.toString();
                    String pDate = dtr.toString();
                    String urlStr = url.toString();
                    String idStr = tId.toString();

                    //assign variables
                    image.add(urlStr);
                    Log.i("test", image.toString());
                    //assign it to the array for the list adapter
                    testArray.add(posting);
                    dateLife.add(pDate);
                    Log.i("test", dateLife.toString());
                    twitId.add(idStr);

                    Log.i("test", "enter a ray text");


                    //Log.d("this is my array", "arr45: " + MainActivity.image.toString());
                }
                //reset list adapter and force reload on listview
                actOnIt.mainListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("this is a JSON error", e.getMessage());
                e.printStackTrace();
            }



        }

    }




}
