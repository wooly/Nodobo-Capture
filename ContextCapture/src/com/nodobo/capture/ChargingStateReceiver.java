package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
// import android.os.ChargingManager;

import java.lang.StringBuilder;

public class ChargingStateReceiver extends BroadcastReceiver
{
    private final String TAG = "ChargingStateReceiver";
    private final String kind = "power";
    private String generator;
    private String data;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED))
        {
		    Logger.log("ContextCapture", "Charging: " + "connected");
		    generator = this.getClass().getName();
		    Clue clue = new Clue(kind, generator, "connected");
        }
        else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
        {
            Logger.log("ContextCapture", "Charging: " + "disconnected");
		    generator = this.getClass().getName();
		    Clue clue = new Clue(kind, generator, "disconnected");
        }
    }
}