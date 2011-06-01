package com.nodobo.capture;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.lang.StringBuilder;

public class ActivityReceiver extends AbstractNodoboReceiver
{
    private final String TAG = "ActivityReceiver";
    private final String kind = "application";
    private String data;
    
    public ActivityReceiver(SQLiteDatabase db)
    {
        super(db);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Clue clue = new Clue(mDb, kind, this.getClass().getName(), intent.getStringExtra("package") + "," + intent.getStringExtra("class"), intent.getLongExtra("time", -1));
    }
}
