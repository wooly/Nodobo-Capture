package com.nodobo.capture;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{    
    static int log(String TAG, String message)
    {
        try {
            Log.d(TAG, message);

    		File logFile = new File("/nodobo/capture/context.log");
    		logFile.createNewFile();
            FileOutputStream logStream = new FileOutputStream(logFile, true);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            Log.d("Logger", result.toString());
        }
        return 0;
    }
}
