package com.nodobo.capture;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.*;

public class DatabaseOpenHelper
{
    public final String TAG = "DatabaseOpenHelper";
    private Context mContext;
    private SQLiteDatabase db = null;
    private final String DATABASE_TABLE_CREATE = "CREATE TABLE clues ("
            + "_id" + " INTEGER PRIMARY KEY,"
            + "kind" + " VARCHAR[16],"
            + "generator" + " VARCHAR[32],"
            + "datetime" + " DATETIME,"
            + "data" + " TEXT"
            + ");"; 
    
    public DatabaseOpenHelper() {
    }
    
    public DatabaseOpenHelper(Context context) {
        mContext=context;
    }

    private void open() {
        File dbDir=null, dbFile=null;
        dbDir = new File(Environment.getExternalStorageDirectory(),"nodobo");
        dbDir.mkdirs();
        dbFile = new File(dbDir, "clues.sqlite3");
        
        if (dbFile.exists()) {
            Log.d(TAG, "Opening database at " + dbFile);
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        }
        else {
            Log.d(TAG, "Creating database at " + dbFile);
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            create();
        }
    }

    public void close() {
        Log.d(TAG, "Closing database");
        db.close();
    }

    private void create() {
        Log.d(TAG, "Creating Database " + db.getPath());
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    public SQLiteDatabase getWritableDatabase() {
        if (db == null)
        {
            Log.d(TAG, "Database is null, opening");
            this.open();
        }
        return db;
    }
}