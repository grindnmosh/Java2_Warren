package com.grinddesign.java2_warren.classgroup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.grinddesign.java2_warren.twitworld.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.classgroup
 * <p/>
 * File:    TParse.Java
 * <p/>
 * Purpose: This is where my app parses my twitter data so that it can be used in the rest of the app. I took the ASYNC and reformatted to a service.
 */

public class TParse {


    //set context
    Context feedCon;

    //set TAG constant
    static String TAG = "NETWORK DATA - FeedMe";

    //define Classes context
    public TParse(Context context) {
        feedCon = context;
    }

    //Toast method to be able to easily assign toasts as needed throughout the app
    public void Toasty(String str) {
        Toast.makeText(feedCon, str, Toast.LENGTH_LONG).show();
    }

    //test for connectivity
    public void twitThis() {

        new loadTwitterToken().execute();
    }

    //set constants for OAUTH
    static final String twitterAPIKEY = "fQOl6VG5yKkqNegDJL6hz7NnX";

    static final String twitterAPISECRET = "H1LsT1XpV72lTkwZXuZebrI6uIw9FRW46jbMaPGg5ymwHTOGbC";

    static final String twitterAPIurl = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

    static final String screenName = "grindnmosh";

    static final int tweets2Return = 20;

    //Create URL to send when OAUTH is successful
    static String tweeterURL = twitterAPIurl + screenName
            + "&include_rts=1&count=" + tweets2Return;

    //set global variables
    String twitterToken;
    String jsonTokenStream;
    StringBuilder builder;

    //start ASync calls
    protected class loadTwitterToken extends AsyncTask<Void, Void, Integer> {

        //requires it to run in background
        @Override
        protected Integer doInBackground(Void... params) {
            //Log.i("test", "in BG");

            try {
                //establish connection
                DefaultHttpClient httpclient = new DefaultHttpClient(
                        new BasicHttpParams());
                //prepare to get authenticated on twitter OAUTH URL
                HttpPost httppost = new HttpPost(
                        "https://api.twitter.com/oauth2/token");

                //encode and submit key and secret
                String apiString = twitterAPIKEY + ":" + twitterAPISECRET;
                String authorization = "Basic "
                        + Base64.encodeToString(apiString.getBytes(),
                        Base64.NO_WRAP);

                //set up security to keep data hidden
                httppost.setHeader("Authorization", authorization);
                httppost.setHeader("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.setEntity(new StringEntity(
                        "grant_type=client_credentials"));

                InputStream inputStream = null;

                //get response from twitter with the access token and convert it to string and assign to token variable
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                jsonTokenStream = sb.toString();

                return 1;
            } catch (Exception e) {
                Log.e("loadTwitterToken",
                        "doInBackground Error:" + e.getMessage());
                return null;
            }
        }

        //process the token and instantiate the method to begin pulling the data
        @Override
        protected void onPostExecute(Integer result) {
            //Log.i("test", "in BG main 2");
            // Extract Token from JSON stream
            try {
                JSONObject root = new JSONObject(jsonTokenStream);
                twitterToken = root.getString("access_token");
            } catch (Exception e) {
                Log.e("loadTwitterToken", "onPost Error:" + e.getMessage());
            }

            // execute getting the data
            new loadTwitterFeed().execute();
        }
    }


    protected class loadTwitterFeed extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            //Log.i("test", "in BG 2");

            //assign constants
            BufferedReader reader = null;
            builder = new StringBuilder();
            JSONArray feedsObject = new JSONArray();

            try {
                //recoonect with twitter this time to the url to pull the JSON data from
                DefaultHttpClient httpClient = new DefaultHttpClient(
                        new BasicHttpParams());

                //call the twitter JSON url we established earlier
                HttpGet httpget = new HttpGet(tweeterURL);

                // set up security
                httpget.setHeader("Authorization", "Bearer " + twitterToken);
                httpget.setHeader("Content-type", "application/json");

                //get response from twitter
                InputStream inputStream = null;
                HttpResponse response = httpClient.execute(httpget);
                HttpEntity entity = response.getEntity();
                Log.i("tweeter", tweeterURL);
                //grab the data from the response
                inputStream = entity.getContent();
                reader = new BufferedReader(
                        new InputStreamReader(inputStream, "UTF-8"), 8);

                //pull the data from the buffer and build it into a string
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                //Log.d("this is my array", "arr: " + builder);

                //assign your string builder to your JSON Array
                feedsObject = new JSONArray(builder.toString());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }


            return feedsObject;

        }

        protected void onPostExecute(JSONArray result) {


            try {
                for (int t=0; t<result.length(); t++) {
                    //reset stringbuilder each time
                    StringBuilder sb = new StringBuilder();
                    StringBuilder url = new StringBuilder();
                    Log.i("test", "enter a ray");

                    //grab object at point in array that you have cycled to
                    JSONObject tweetObject = result.getJSONObject(t);
                    //Log.i("test", "enter a ray2");
                    try {
                        //grab the data you want to use

                        sb.append(tweetObject.getString("text"));
                        url.append(tweetObject.getJSONObject("user").getString("profile_image_url"));

                        //Log.i("url", url.toString());

                        sb.append("\n");
                        sb.append("\n");
                        sb.append(tweetObject.getString("created_at"));
                        sb.append("\n");

                        Log.i("url bs", sb.toString());
                        //give it some space with a break

                        Log.i("test", "enter a ray3");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sb.append("any random text");
                    }
                    //load the object pulled into a string
                    String posting = sb.toString();
                    String urlStr = url.toString();

                    //MainActivity.image.add(urlStr);
                    Log.i("test", "enter a ray img");
                    //assign it to the array for the list adapter
                    MainActivity.testArray.add(posting);
                    Log.i("test", "enter a ray text");


                    //Log.d("this is my array", "arr45: " + MainActivity.image.toString());
                }//reset list adapter and force reload on listview

                MainActivity.mainListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("this is a JSON error", e.getMessage());
                e.printStackTrace();
            }


        }


    }


}
