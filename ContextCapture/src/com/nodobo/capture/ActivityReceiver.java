package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.StringBuilder;

public class ActivityReceiver extends BroadcastReceiver
{
    private final String TAG = "ActivityReceiver";
    private final String kind = "application";
    private String data;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.acmon.activity"))
        {
            Clue clue = new Clue(kind, this.getClass().getName(), intent.getStringExtra("package") + "," + intent.getStringExtra("class"), intent.getLongExtra("time", -1));
            Log.d(TAG, "Caught activity: package=" + intent.getStringExtra("package") + " class=" + intent.getStringExtra("class"));
        }
    }
}
