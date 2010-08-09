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
    private final String kind = "userInteraction";
    private final String generator = "InteractionReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.interaction.virtual_key"))
        {
            Log.d(TAG, "Virtual Key pressed: " + intent.getStringExtra("button"));
            notifyQuirp();
            Clue clue = new Clue("virtualkey", generator, intent.getStringExtra("button"), intent.getLongExtra("time", 0));
        }
        if (intent.getAction().equals("android.intent.interaction.hardware_key"))
        {
            Log.d(TAG, "Hardware Key pressed: " + intent.getStringExtra("button") + " " + intent.getStringExtra("state"));            
            notifyQuirp();
            Clue clue = new Clue("hardwarekey", generator, intent.getStringExtra("button"), intent.getLongExtra("time", 0));
        }
        if (intent.getAction().equals("android.intent.interaction.touch"))
        {
            Log.d(TAG, "Touch Screen Interaction: (" + intent.getIntExtra("xpos", 0) + "," + intent.getIntExtra("ypos", 0) + ")");
            Clue clue = new Clue("touch", generator, intent.getIntExtra("xpos", 0) + "," + intent.getIntExtra("ypos", 0), intent.getLongExtra("time", 0));
            notifyQuirp();
        }
    }
    
    public native int notifyQuirp();
}