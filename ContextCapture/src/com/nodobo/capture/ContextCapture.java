package com.nodobo.capture;

import java.io.File;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import android.app.Service;
import android.os.Environment;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ContextCapture extends Service implements SensorEventListener
{
    public final String TAG = "ContextCapture";
    public SQLiteDatabase mDb = null;
    
    private String sensorName;
    private boolean captureState = false;
    private SensorManager sensorManager;
    
    private LocationManager mLocationManager;
    
    @Override
    public void onCreate()
    {
        super.onCreate();

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
    
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        
        File nodoboDirectory = new File(Environment.getExternalStorageDirectory() + "nodobo/");
        nodoboDirectory.mkdirs();
        
        DatabaseOpenHelper doh = new DatabaseOpenHelper();
        mDb = doh.getWritableDatabase();

        locationSetup();

        registerActivityReceiver();
        registerBatteryStateReceiver();
        registerChargingStateReceiver();
        registerHeadphoneStateReceiver();
        registerImeReceiver();
        registerInteractionReceiver();
        registerOrientationReceiver();
        registerScreenStateReceiver();
        
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> accSensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        Sensor accSensor = accSensors.get(0);
        // if (accSensor != null) sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_UI);

        List<Sensor> proxSensors = sm.getSensorList(Sensor.TYPE_PROXIMITY);
        Sensor proxSensor = proxSensors.get(0);
        if (proxSensor != null) sm.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
        
    public void onAccuracyChanged (Sensor sensor, int accuracy)
    {
    }
    
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        long timestamp = System.currentTimeMillis();
        float[] values = sensorEvent.values;
        String generator = this.getClass().getName();

        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            // Clue clue = new Clue("proximity", generator, "" + values[0], timestamp);
        }
        else
        {
            // Clue clue = new Clue("acceleration", generator, "" + values[0] + "," + values[1] + "," + values[2], timestamp);
        }
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    
    @Override
    public void onDestroy()
    {
        mDb.close();
    }

    private void locationSetup()
    {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocationListener = new NodoboLocationListener();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, mLocationListener);
    }
    
    private void registerActivityReceiver()
    {
        Logger.log(TAG, "Registering Activity Receiver");
        IntentFilter filter = new IntentFilter("com.nodobo.intent.activity");
        ActivityReceiver mReceiver = new ActivityReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }

    private void registerBatteryStateReceiver()
    {
        Logger.log(TAG, "Registering Battery State Receiver");
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BatteryStateReceiver mReceiver = new BatteryStateReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }

    private void registerChargingStateReceiver()
    {
        Logger.log(TAG, "Registering Charging State Receiver");
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        ChargingStateReceiver mReceiver = new ChargingStateReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }

    private void registerHeadphoneStateReceiver()
    {
        Logger.log(TAG, "Registering Headphone State Receiver");
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        HeadphoneStateReceiver mReceiver = new HeadphoneStateReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }

    private void registerImeReceiver()
    {
        Logger.log(TAG, "Registering IME Receiver");
        IntentFilter filter = new IntentFilter("com.nodobo.intent.ime.visible");
        filter.addAction("com.nodobo.intent.ime.invisible");
        filter.addAction("com.nodobo.intent.ime.keypress");
        ImeReceiver mReceiver = new ImeReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }

    private void registerInteractionReceiver()
    {
        Logger.log(TAG, "Registering Interaction Receiver");
        IntentFilter filter = new IntentFilter("com.nodobo.intent.touch");
        InteractionReceiver mReceiver = new InteractionReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }

    private void registerOrientationReceiver()
    {
        Logger.log(TAG, "Registering Orientation Receiver");
        IntentFilter filter = new IntentFilter("com.nodobo.intent.rotation");
        OrientationReceiver mReceiver = new OrientationReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }

    private void registerScreenStateReceiver()
    {
        Logger.log(TAG, "Registering Screen State Receiver");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        ScreenStateReceiver mReceiver = new ScreenStateReceiver(mDb);
        registerReceiver(mReceiver, filter);
    }
}
