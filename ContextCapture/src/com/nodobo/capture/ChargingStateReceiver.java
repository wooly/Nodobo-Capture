package com.nodobo.capture;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class ChargingStateReceiver extends AbstractNodoboReceiver
{
    private final String TAG = "ChargingStateReceiver";
    private final String kind = "power";
    private String generator;
    private String data;
    
    public ChargingStateReceiver(SQLiteDatabase db)
    {
        super(db);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
	    generator = this.getClass().getName();
	    String state = "unknown";

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED))
            state = "connected";
        else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
            state = "disconnected";

	    Clue clue = new Clue(mDb, kind, generator, state, System.currentTimeMillis());
    }
}