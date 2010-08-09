package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import java.lang.StringBuilder;

public class BatteryStateReceiver extends BroadcastReceiver
{
    private final String TAG = "BatteryStateReceiver";
    private final String kind = "batteryLevel";
    private String generator;
    private String data;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))
        {
		    int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int level = -1;  // percentage, or -1 for unknown
            if (rawlevel >= 0 && scale > 0)
                level = (rawlevel * 100) / scale;
		    
		    Logger.log("ContextCapture", "Battery level: " + level);
		    generator = this.getClass().getName();
		    Clue clue = new Clue(kind, generator, "" + level);
        }
    }
}
