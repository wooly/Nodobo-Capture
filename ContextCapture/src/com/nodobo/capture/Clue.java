package com.nodobo.capture;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Clue
{
    public final String TAG = "Clue";    
    private static final String DATABASE_TABLE_NAME = "clues";    
    private ContentValues values = new ContentValues();
	
    public Clue(SQLiteDatabase db, String kind, String generator, String data, long timestamp) {
	    values.put("datetime", timestamp);
		values.put("kind", kind);
		values.put("generator", generator);
		values.put("data", data);
		Logger.log(TAG, "Clue: " + timestamp + " " + kind + " " + generator + " " + data);
		store(db);
	};
	
    private long store(SQLiteDatabase mDb) {
	    long rowId = mDb.insert(DATABASE_TABLE_NAME, "TEXT", values);
        if (rowId > 0)
            return rowId;
        
        throw new SQLiteException("Failed to insert row into clues database");
	}
}