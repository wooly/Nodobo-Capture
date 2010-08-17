package com.nodobo.capture;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class Clue
{
    public final String TAG = "Clue";
    
    private static final String DATABASE_TABLE_NAME = "clues";
    private static final String DATABASE_PATH = "/nodobo/capture/clues.sqlite3";
    
    private ContentValues values = new ContentValues();
	private static String date;
	
    public Clue(String kind, String generator, String data) {
		values.put("datetime", System.currentTimeMillis());
		values.put("kind", kind);
		values.put("generator", generator);
		values.put("data", data);

		Logger.log("ContextCapture", "Clue created.");
		
		store();
	};

    public Clue(String kind, String generator, String data, long timestamp) {
		values.put("datetime", timestamp);
		values.put("kind", kind);
		values.put("generator", generator);
		values.put("data", data);
		
		store();
	};
	
    private long store() {
	    SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
	    long rowId = db.insert(DATABASE_TABLE_NAME, "TEXT", values);
	    db.close();
        if (rowId > 0)
        {
    	    Logger.log("ContextCapture", "Clue stored.");
            return rowId;
        }
        
        throw new SQLiteException("Failed to insert row into clues database");
	}
}