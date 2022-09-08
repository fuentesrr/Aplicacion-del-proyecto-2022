package com.example.proyects1;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Localizacion implements LocationListener {
    public static double Late;
    public static double Longe;
    public static long TimeVar;
    MainActivity mainActivity;

    public MainActivity getMainActivity() {
        return mainActivity;
    }
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    public void onLocationChanged(Location loc) {

        Late = loc.getLatitude();
        Longe = loc.getLongitude();
        TimeVar = loc.getTime();
        this.mainActivity.setLocation(loc);

    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mainActivity.getBaseContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mainActivity.getBaseContext(), "GPS Activado", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }
}