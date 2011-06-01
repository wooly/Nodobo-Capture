package com.nodobo.capture;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;

public class BatteryStateReceiver extends AbstractNodoboReceiver
{
    private final String TAG = "BatteryStateReceiver";
    private final String kind = "battery";
    private String generator;
    private String data;
    
    public BatteryStateReceiver(SQLiteDatabase db)
    {
        super(db);
    }
    
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
		    
		    generator = this.getClass().getName();
            Clue clue = new Clue(mDb, kind, generator, "" + level, System.currentTimeMillis());
        }
    }
}
