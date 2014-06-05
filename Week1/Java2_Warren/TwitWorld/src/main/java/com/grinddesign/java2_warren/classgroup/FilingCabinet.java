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

    public String readingIt(Context context, String string) {
        Log.i("READER SHIT", "ENTRY");


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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert pullIt != null;
                pullIt.close();
            } catch (IOException e) {
                Log.e("CLOSE FILE ERROR", e.toString());
            }
        }
        Log.i("HOLY WOW", content);
        return content;
    }

}


