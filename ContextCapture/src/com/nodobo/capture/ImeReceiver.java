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
        if (intent.getAction().equals("com.nodobo.intent.ime.visible"))
        {
            Clue clue = new Clue(kind, generator, "visible");
            Log.d(TAG, "Caught signal: IME visible");
        }
        if (intent.getAction().equals("com.nodobo.intent.ime.invisible"))
        {
            Clue clue = new Clue(kind, generator, "invisible");
            Log.d(TAG, "Caught signal: IME invisible");
        }
        if (intent.getAction().equals("com.nodobo.intent.ime.keypress"))
        {
            int key = intent.getIntExtra("key", 0);
            switch (key)
            {
                case -6: data = "<alt>"; break;
                case -5: data = "<del>"; break;
                case -4: data = "<done>"; break;
                case -3: data = "<cancel>"; break;
                case -2: data = "<mode>"; break;
                case -1: data = "<shift>"; break;
                case 32: data = "<space>"; break;
                default: data = "" + (char) key; break;
            }
            Clue clue = new Clue("keypress", generator, data, intent.getLongExtra("time", 0));
            Log.d(TAG, "Caught signal: IME press - " + data);
        }
    }
}