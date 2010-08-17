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
            char key = intent.getCharExtra("key", (char) 0x00);
            if (key == (char) 0x20) data = "<space>";
            else data = "" + key;
            Clue clue = new Clue("keypress", generator, data, intent.getLongExtra("time", 0));
            Log.d(TAG, "Caught signal: IME press - " + data);
        }
    }
}