package com.grinddesign.java2_warren.classgroup;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:  Robert Warren
 * <p/>
 * Project:  Java2_Warren
 * <p/>
 * Package: com.grinddesign.java2_warren.classgroup
 * <p/>
 * File:    FilingCabinet.Java
 * <p/>
 * Purpose: This is where I process the writing of strings to a local file and read thos e files from the specific calls to each.
 */
public class FilingCabinet {

    private static FilingCabinet t_instance;

    //constructor
    private FilingCabinet() {

    }

    //This is my singleton instantiation
    public static FilingCabinet getInstance() {
        if (t_instance == null) {
            t_instance = new FilingCabinet();
        }
        return t_instance;
    }

    /**
     * This is my method to save to file
     */
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

    /**
     * This is my method to read my previously saved data
     */
    public String readingIt(Context context, String string) throws InterruptedException {
        Log.i("READER CHECK", "ENTRY");
        Thread.sleep(4500);

        String content = null;

        FileInputStream pullIt = null;

        try {
            pullIt = context.openFileInput(string);
            BufferedInputStream buff = new BufferedInputStream(pullIt);
            byte[] contentB = new byte[1024];
            int bytesRead = 0;
            StringBuilder conBuff = new StringBuilder();

            while ((bytesRead = buff.read(contentB)) != -1) {
                content = new String(contentB, 0, bytesRead);
                conBuff.append(content);
            }
            content = conBuff.toString();
            Log.i("traveler", content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i("test", "test");
            try {
                if (pullIt != null) {
                    pullIt.close();
                }
            } catch (IOException e) {
                Log.e("CLOSE FILE ERROR", e.toString());
            }
        }
        Log.i("HOLY WOW", content);
        return content;
    }

}


