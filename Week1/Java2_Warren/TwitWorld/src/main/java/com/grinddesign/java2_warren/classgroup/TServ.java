package com.grinddesign.java2_warren.classgroup;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.grinddesign.java2_warren.twitworld.MainActivity;

public class TServ extends Service {
    Context poopoo = this;
    public TServ() {
    }
    String jsonStr;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "create service", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jsonStr = MainActivity.feedArray.toString();
        Log.i("testing", jsonStr);
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int StartId) {
        try {
            Thread.sleep(500);
            Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();
            jsonStr = MainActivity.feedArray.toString();
            Log.i("testing", jsonStr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //save file here
        return START_REDELIVER_INTENT;

    }

}
