package com.grinddesign.java2_warren.classgroup;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

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
 * File:    TIntServ.Java
 * <p/>
 * Purpose:
 */

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TIntServ extends IntentService {
    public TIntServ() {
        super("TIntServ");
    }

    Context feedCon = this;

    FilingCabinet t_File;

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
    String fileName = "string_from_twitter";



    @Override
    protected void onHandleIntent(Intent intent) {

        t_File = FilingCabinet.getInstance();

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

        } catch (Exception e) {
            Log.e("loadTwitterToken",
                    "doInBackground Error:" + e.getMessage());
        }
        try {
            JSONObject root = new JSONObject(jsonTokenStream);
            twitterToken = root.getString("access_token");
        } catch (Exception e) {
            Log.e("loadTwitterToken", "onPost Error:" + e.getMessage());
        }
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


            //assign your string builder to your JSON Array
            feedsObject = new JSONArray(builder.toString());
            Log.d("this is my array", "arrline: " + feedsObject);
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
        //fileName = feedsObject.toString();

        Log.i("feeder", "arrfighter:" + fileName);

        Messenger messy = (Messenger) intent.getExtras().get("messenger");

        Message msg = Message.obtain();

        msg.arg1 = Activity.RESULT_OK;

        msg.obj = fileName;

        try {
            messy.send(msg);
        } catch (RemoteException e) {
            Log.e(getClass().getName(), "Message Exception:", e);
        }

        Log.i("FILE NAME", msg.obj.toString());

        t_File.writeItUp(feedCon, fileName, feedsObject.toString());




    }
}
