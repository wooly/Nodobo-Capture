package com.nodobo.capture;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{    
    static int log(String TAG, String message)
    {
        try {
    		File logFile = new File("/aios/context/context.log");
            FileOutputStream logStream = new FileOutputStream(logFile, true);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            Log.d(TAG, message);
        
            StringBuffer sb = new StringBuffer();
            sb.append(dateFormat.format(new Date()));
            sb.append(" - ");
            sb.append(TAG);
            sb.append(": ");
            sb.append(message);
            sb.append("\n");
        
            logStream.write(sb.toString().getBytes("UTF-8"));
            logStream.close();
        } catch (Exception e) {
            Log.d("Logger", "Exception: " + e.getMessage());
        }
        return 0;
    }
}
