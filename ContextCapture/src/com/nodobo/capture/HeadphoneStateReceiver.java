package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.StringBuilder;

public class HeadphoneStateReceiver extends BroadcastReceiver
{
    private final String TAG = "HeadphoneStateReceiver";
    private final String kind = "headphones";
    private String generator;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG))
        {
            int state = intent.getIntExtra("state", 0);
		    Logger.log("ContextCapture", "Headphone state: " + state);
		    generator = this.getClass().getName();
		    Clue clue = new Clue(kind, generator, "" + state);
        }
    }
}