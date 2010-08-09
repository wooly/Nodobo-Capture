package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.StringBuilder;

public class ImeReceiver extends BroadcastReceiver
{
    private final String TAG = "ImeReceiver";
    private final String kind = "imeState";
    private final String generator = "ImeReceiver";
    private String data;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.ime.visible"))
        {
            Clue clue = new Clue(kind, generator, "visible");
            Log.d(TAG, "Caught signal: IME visible");
        }
        if (intent.getAction().equals("android.intent.ime.invisible"))
        {
            Clue clue = new Clue(kind, generator, "invisible");
            Log.d(TAG, "Caught signal: IME invisible");
        }
        if (intent.getAction().equals("android.intent.ime.keypress"))
        {
            Clue clue = new Clue("keypress", generator, intent.getStringExtra("key"), intent.getLongExtra("time", 0));
            Log.d(TAG, "Caught signal: IME press - " + intent.getStringExtra("key"));
        }
    }
}