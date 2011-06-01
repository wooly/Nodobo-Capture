package com.nodobo.capture;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class OrientationReceiver extends AbstractNodoboReceiver
{
    private final String TAG = "OrientationReceiver";
    private final String kind = "orientation";
    private String generator;
    private String data;
    
    public OrientationReceiver(SQLiteDatabase db)
    {
        super(db);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Clue clue = new Clue(mDb, kind, this.getClass().getName(), "" + intent.getIntExtra("state", -1), intent.getLongExtra("time", -1));
    }
}