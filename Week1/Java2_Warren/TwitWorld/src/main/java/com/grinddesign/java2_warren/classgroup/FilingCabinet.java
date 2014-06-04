package com.grinddesign.java2_warren.classgroup;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.classgroup
 * <p/>
 * File:    ${File_Name}
 * <p/>
 * Purpose: ${Comments_Here}
 */
public class FilingCabinet {

    private static FilingCabinet t_instance;

    //constructor
    private FilingCabinet() {

    }

    public static FilingCabinet getInstance() {
        if (t_instance == null) {
            t_instance = new FilingCabinet();
        }
        return t_instance;
    }

    public boolean writeItUp(Context context, String fileName, String content) {
        boolean result = false;
        FileOutputStream streamyFile = null;

        try {
            streamyFile = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            streamyFile.write(content.getBytes());
            Log.i("WRITE FILE", "SUCCESS");
        } catch (IOException e) {
            Log.e("WRITE FILE ERROR", e.toString());
        }




        return result;
    }
}


