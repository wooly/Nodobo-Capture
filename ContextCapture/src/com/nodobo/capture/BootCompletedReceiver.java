package com.nodobo.capture;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver
{
    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        Logger.log("ContextCapture", action);
        if(action == null)
            Log.e(TAG,"Action==null!");
        else if("android.intent.action.BOOT_COMPLETED".equals(action))
        {
            Intent i = new Intent();
            i.setClassName("com.nodobo.capture", "com.nodobo.capture.ContextCapture");
            
            ComponentName cname = context.startService(i);
            if(cname == null)
                Log.e(TAG,"BackgroundService was not started");
            else
                Logger.log("ContextCapture","BackgroundService started");
        }
    }
}