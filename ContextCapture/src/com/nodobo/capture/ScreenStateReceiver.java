package com.nodobo.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.StringBuilder;

public class ScreenStateReceiver extends BroadcastReceiver
{
    private final String TAG = "ScreenStateReceiver";
    private final String kind = "screenState";
    private String generator;
    private String data;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            generator = this.getClass().getName();            
            Clue clue = new Clue(kind, generator, "on");
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            generator = this.getClass().getName();            
            Clue clue = new Clue(kind, generator, "off");
        }
    }
}