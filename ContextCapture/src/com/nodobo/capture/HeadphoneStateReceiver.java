package com.nodobo.capture;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.lang.StringBuilder;

public class HeadphoneStateReceiver extends AbstractNodoboReceiver
{
    private final String TAG = "HeadphoneStateReceiver";
    private final String kind = "headphones";
    private String generator;

    public HeadphoneStateReceiver(SQLiteDatabase db)
    {
        super(db);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int state = intent.getIntExtra("state", 0);
	    generator = this.getClass().getName();
	    Clue clue = new Clue(mDb, kind, generator, "" + state, System.currentTimeMillis());
    }
}