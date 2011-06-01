package com.nodobo.capture;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.lang.StringBuilder;

public class ScreenStateReceiver extends AbstractNodoboReceiver
{
    private final String TAG = "ScreenStateReceiver";
    private final String kind = "screen";
    private String generator;
    private String data;
    
    public ScreenStateReceiver(SQLiteDatabase db)
    {
        super(db);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String state = "unknown";
        generator = this.getClass().getName();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
            state = "on";
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
            state = "off";
        Clue clue = new Clue(mDb, kind, generator, state, System.currentTimeMillis());
    }
}