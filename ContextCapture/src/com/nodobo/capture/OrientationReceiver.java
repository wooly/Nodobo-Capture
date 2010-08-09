package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.StringBuilder;

public class OrientationReceiver extends BroadcastReceiver
{
    private final String TAG = "OrientationReceiver";
    private final String kind = "orientation";
    private String generator;
    private String data;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("com.nodobo.intent.rotation"))
        {
            generator = this.getClass().getName();            
            Clue clue = new Clue(kind, generator, "" + intent.getIntExtra("state", -1), intent.getLongExtra("time", -1));
        }
    }
}