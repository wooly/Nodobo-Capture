package com.nodobo.capture;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class NodoboLocationListener implements LocationListener
{
    private String generator = this.getClass().getName();
    private String kind = "location";
    
    @Override
    public void onLocationChanged(Location loc)
    {
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        Clue clue = new Clue(kind, generator, "" + latitude + "," + longitude, System.currentTimeMillis());
    }
    
    @Override
    public void onProviderEnabled(String provider)
    {}
    
    @Override
    public void onProviderDisabled(String provider)
    {}
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {}   
}

