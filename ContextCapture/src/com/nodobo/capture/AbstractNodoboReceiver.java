package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractNodoboReceiver extends BroadcastReceiver
{
    public SQLiteDatabase mDb;
    
    public AbstractNodoboReceiver(SQLiteDatabase db)
    {
        super();
        mDb = db;
    }
}
