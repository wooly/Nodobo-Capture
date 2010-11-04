package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import java.lang.StringBuilder;

public class InteractionReceiver extends BroadcastReceiver
{
    private final String TAG = "InteractionReceiver";
    private final String kind = "interaction";
    private String generator;
    
    public InteractionReceiver()
    {
        super();
        System.loadLibrary("notifier");
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        generator = this.getClass().getName();
        if (intent.getAction().equals("com.nodobo.intent.softkey"))
        {
            Log.d(TAG, "Virtual Key pressed: " + intent.getStringExtra("button"));
            notifyQuirp();
            Clue clue = new Clue("virtualkey", generator, intent.getStringExtra("button"), intent.getLongExtra("time", 0));
        }
        if (intent.getAction().equals("com.nodobo.intent.hardkey"))
        {
            Log.d(TAG, "Hardware Key pressed: " + intent.getStringExtra("button") + " " + intent.getStringExtra("state"));            
            notifyQuirp();
            Clue clue = new Clue("hardwarekey", generator, intent.getStringExtra("button"), intent.getLongExtra("time", 0));
        }
        if (intent.getAction().equals("com.nodobo.intent.touch"))
        {
            Log.d(TAG, "Touch Screen Interaction: (" + intent.getIntExtra("xpos", 0) + "," + intent.getIntExtra("ypos", 0) + "," + intent.getFloatExtra("pressure", (float) 50.0) + ")");
            Clue clue = new Clue("touch", generator, intent.getIntExtra("xpos", 0) + "," + intent.getIntExtra("ypos", 0) + "," + intent.getFloatExtra("pressure", (float) 50.0), intent.getLongExtra("time", 0));
            notifyQuirp();
        }
    }
    
    public native int notifyQuirp();
}