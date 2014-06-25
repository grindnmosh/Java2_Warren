package com.grinddesign.java2_warren.twitworld;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
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

import static android.content.SharedPreferences.Editor;

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

public class MainActivity extends Activity implements MainActivityFragment.onListClicked, DetailActivityFragment.grind, DetailActivityFragment.face, DetailActivityFragment.beRated, SearchView.OnQueryTextListener {



    public static Bundle broken;
    public static Bundle mainLove;
    Context thisHere = this;
    static FilingCabinet x_File;
    static String fileName = "string_from_twitter";
    final HandleMe tHand = new HandleMe(this);
    SharedPreferences sharedpreferences;
    public static SharedPreferences preferences;
    public static Editor edit;
    public static TextView userlicious;
    JSONArray list;

    public enum DialogType {SEARCH, PREFERENCES, FAVORITES, ABOUT}

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





        sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Log.i("TESTPREFS", sharedpreferences.toString());

        EditText username = (EditText) findViewById(R.id.username);
        userlicious = (TextView) findViewById(R.id.user);


        preferences = PreferenceManager.getDefaultSharedPreferences(thisHere);
        edit = preferences.edit();
        if (preferences.getString("username", "").isEmpty()) {
            Log.i("PREFTEST", "ENTERED");
            edit.putString("username", "guest");
            edit.apply();
        }

        else if (!preferences.getString("username", "").isEmpty()) {
            Log.i("PREFTEST", "ENTERED");
            userlicious.setText("Logged In As " + preferences.getString("username", ""));
        }



        //check for saved instance
        //if there is a saved instance
        if( savedInstanceState != null ) {


            if (broken != null) {
                String reloadString = broken.getString("detail_message");
                fragment.loadItUp(reloadString);
            }

            //data from saved instance
            //String reloadString = savedInstanceState.getString("detail_message");

            String passerBy = mainLove.getString("message");
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

                //This creates my intent and starts my service.
                Messenger serviceMessenger = new Messenger(tHand);
                Intent intent = new Intent(thisHere, TIntServ.class);
                intent.putExtra("messenger", serviceMessenger);
                startService(intent);
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

    public void saveItAll(String tId, String result) {

        //s_File = FilingCabinet.getInstance();

        String saveMe = "Twitter ID " + tId + " was ranked " + result + " Stars";

        Log.i("SAVEDME", saveMe);

        JSONObject obj = new JSONObject();
        list = new JSONArray();
        JSONObject info = new JSONObject();
        Log.i("SAVEDME", "check 1");
        try {
            info.put("stars", saveMe);
            list.put(info);
            obj.put("starryNights", list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("SAVEDME", list.toString());

        //FilingCabinet.getInstance().writeItUp(thisHere, "starry_night", list.toString());
    }

    public void launchDialogFragment(DialogType type) {
        AlertDialogFragment dialogFrag = AlertDialogFragment.newInstance(type);
        dialogFrag.show(getFragmentManager(), "search_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onQueryTextChange(String newText)
    {
        final ListView lv = (ListView) findViewById(R.id.tList);
        // this is your adapter that will be filtered
        if (TextUtils.isEmpty(newText))
        {
            lv.clearTextFilter();
        }
        else
        {
            lv.setFilterText(newText);
            Log.i("text", newText);
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "tapped Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuPreferences:
                Toast.makeText(this, "tapped Preferences", Toast.LENGTH_SHORT).show();
                launchDialogFragment(DialogType.PREFERENCES);
                break;
            case R.id.menuFavorite:
                //setContentView(R.layout.activity_starry);
                Intent StarredOut = new Intent(this, StarActivity.class);
                startActivityForResult(StarredOut, 1);
                //Toast.makeText(this, StarredOut.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuAbout:
                //setContentView(R.layout.about_me);
                Intent aboutMeAct = new Intent(thisHere, AboutMeActivity.class);
                Log.i("TAPPED OUT", "Reaching For Me");
                thisHere.startActivity(aboutMeAct);
                //Toast.makeText(this, aboutMeAct.toString(), Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }




    /**
     * This Method receives the return intent and populates it in an Alert Box
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if (resultCode == RESULT_OK) {
                Log.i("BITEONE", "TEST");
                String result = data.getStringExtra("result");
                String title = "Robert's Tweet Rating by you";
                Log.i("RESULT", result);
                //create alert to display info passed back in the format of my choosing
                AlertDialog.Builder displayResult = new AlertDialog.Builder(thisHere);
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

    /**********************************************************************************************************************************************************************************************************************************
     * Interface implementations from fragments
     */

    /**
     * This Method handles loading of data based on item selected from the listview
     */

    @Override
    public void listItemSelected(String str) {

        DetailActivityFragment fragment = (DetailActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentDetail);

        if (fragment != null && fragment.isInLayout()) {

            fragment.loadItUp(str);
            Log.i("WILDMAN", str);
        }
        else {

            //Log.i("Click It", str);
            Intent detailPass = new Intent(thisHere, DetailActivity.class);
            Log.i("Cray Cray", str);
            detailPass.putExtra("file name", str);
            Log.i("Cray Cray", detailPass.toString());
            startActivityForResult(detailPass, 1);
            Log.i("WILDMAN", str);
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
     * This method handles the star ratings from the detail fragment
     */
    @Override
    public void starryEyes(String str) {
        String title = "Robert's Tweet Rating by you";
        Log.i("RESULT", str);
        //create alert to display info passed back in the format of my choosing
        AlertDialog.Builder displayResult = new AlertDialog.Builder(thisHere);
        displayResult.setTitle(title).setMessage("You Rated this tweet a " + str + " and we thank you for taking the time to rate our tweet").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        displayResult.create();
        displayResult.show();
    }


    /********************************************************************************************************************************************************************************************************************
     * end of interface implementations from fragments
     */

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
                MainActivityFragment.goldenArray = readIt;
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
                    MainActivityFragment.image.add(urlStr);
                    Log.i("test", MainActivityFragment.image.toString());
                    //assign it to the array for the list adapter
                    MainActivityFragment.testArray.add(posting);
                    MainActivityFragment.dateLife.add(pDate);
                    Log.i("test", MainActivityFragment.dateLife.toString());
                    MainActivityFragment.twitId.add(idStr);

                    Log.i("test", "enter a ray text");


                    //Log.d("this is my array", "arr45: " + MainActivity.image.toString());
                }
                //reset list adapter and force reload on listview
             MainActivityFragment.mainListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("this is a JSON error", e.getMessage());
                e.printStackTrace();
            }



        }

    }

    public static class AlertDialogFragment extends DialogFragment {

        public static DialogType type;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.i("AMIHERE", "YESSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            //final ArrayList<String> edit = new ArrayList<String>();
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflator = getActivity().getLayoutInflater();
            Log.i("AMIHERE2", "YESSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            switch (type) {
                case PREFERENCES:
                    alertBuilder.setView(inflator.inflate(R.layout.login_dialog, null))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {



                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Dialog dialogView = getDialog();
                                    EditText user = (EditText) dialogView.findViewById(R.id.username);
                                    Log.i("AMIHERECLICK", "YESSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                                    if (user != null) {

                                        edit.putString("username", user.getText().toString());
                                        Log.i("AMICLICK1A", edit.toString());
                                        Log.i("AMIHERECLICK2", "YESSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                                        edit.apply();
                                        userlicious.setText("Logged In As " + preferences.getString("username", ""));
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialogFragment.this.getDialog().cancel();
                                }
                            });
                    //Toast.makeText(MainActivity.this, "username = " + preferences.getString("username", ""), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return alertBuilder.create();
        }
        public static AlertDialogFragment newInstance(DialogType dialogType) {
            type = dialogType;
            return new AlertDialogFragment();
        }


    }




}
