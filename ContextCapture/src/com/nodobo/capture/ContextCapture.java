package com.nodobo.capture;

import java.io.File;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import android.app.Service;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ContextCapture extends Service implements SensorEventListener
{
    public final String TAG = "ContextCapture";
    
    private String sensorName;
    private boolean captureState = false;
    private SensorManager sensorManager;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        String database_path = new String("/nodobo/capture/clues.sqlite3");
		File basePathFile = new File(database_path);

        if (basePathFile.exists() == false)
        {
            Logger.log("ContextCapture", "Database does not exist.");
            try
            {
                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(database_path, null); 
                String CLUES_TABLE_CREATE = "CREATE TABLE clues ("
                        + "_id" + " INTEGER PRIMARY KEY,"
                        + "kind" + " VARCHAR[16],"
                        + "generator" + " VARCHAR[32],"
                        + "datetime" + " DATETIME,"
                        + "data" + " TEXT"
                        + ");"; 
                database.execSQL(CLUES_TABLE_CREATE);
                database.close();
            }
            catch (SQLiteException ex)
            {
                Logger.log("ContextCapture", "ERROR: " + ex.getMessage());
            }
            Logger.log("ContextCapture", "Database created!");
        }
        else
        {
            Logger.log("ContextCapture", "Database exists, continuing");
        }
        
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> accSensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        Sensor accSensor = accSensors.get(0);
        // if (accSensor != null) sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_UI);

        List<Sensor> proxSensors = sm.getSensorList(Sensor.TYPE_PROXIMITY);
        Sensor proxSensor = proxSensors.get(0);
        if (proxSensor != null) sm.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
        
    public void onAccuracyChanged (Sensor sensor, int accuracy) {
    }
    
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        float[] values = sensorEvent.values;
        Clue clue = new Clue("acceleration",
                             this.getClass().getName(),
                             "" + values[0] + "," + values[1] + "," + values[2],
                             System.currentTimeMillis());
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
