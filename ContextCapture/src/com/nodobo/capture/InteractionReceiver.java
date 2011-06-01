package com.nodobo.capture;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.lang.StringBuilder;

public class InteractionReceiver extends AbstractNodoboReceiver
{
    private final String TAG = "InteractionReceiver";
    private final String kind = "interaction";
    private String generator;
        
    public InteractionReceiver(SQLiteDatabase db)
    {
        super(db);
        System.loadLibrary("notifier");
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        generator = this.getClass().getName();
        long timestamp = intent.getLongExtra("time", System.currentTimeMillis()); // Second argument is the default, don't want this to be 0.
        if (intent.getAction().equals("com.nodobo.intent.softkey"))
        {
            Clue clue = new Clue(mDb, "virtualkey", generator, intent.getStringExtra("button"), timestamp);
        }
        if (intent.getAction().equals("com.nodobo.intent.hardkey"))
        {
            Clue clue = new Clue(mDb, "hardwarekey", generator, intent.getStringExtra("button"), timestamp);
        }
        if (intent.getAction().equals("com.nodobo.intent.touch"))
        {
            Clue clue = new Clue(mDb, "touch", generator, intent.getIntExtra("xpos", 0) + "," + intent.getIntExtra("ypos", 0) + "," + intent.getFloatExtra("pressure", (float) 50.0), timestamp);
        }
        notifyQuirp();
    }
    
    public native int notifyQuirp();
}