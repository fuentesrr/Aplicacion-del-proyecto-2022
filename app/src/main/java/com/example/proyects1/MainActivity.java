package com.example.proyects1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import java.util.List;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat;
    int PUERTO, PUERTO2, PUERTO3;
    String Date, TimeVar, PuertoString, PuertoString2, PuertoString3;
    UDP_Thread udp_Thread;

    private static final int REQUEST_PERMISSION_LOCATION = 100;
    private final int delay = 5000;
    private boolean Status = false;
    ToggleButton BtSend;
    Handler handler = new Handler();
    android.widget.TextView Latitud, TimeTXT;
    InetAddress IPaddress, IPaddress2, IPaddress3;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Verificar permisos del Ubicacion
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }

        //Variables de la parte grafica
        BtSend = (ToggleButton) findViewById(R.id.btSend);
        Latitud = (TextView) findViewById(R.id.txtLatitud);
        TimeTXT = (TextView) findViewById(R.id.txtTime);

        //FLPC
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationStart();

        BtSend.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                    if (BtSend.isChecked()) {
                        Status = true;
                    } else {
                        Status = false;
                    }
                    Send_UDP();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET}, 1000);
            }

        });
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }

    public void setLocation(Location loc) {
        Latitud.setText(" " + Localizacion.Late + ", " + Localizacion.Longe);
        TimeTXT.setText(" " + new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Localizacion.TimeVar));

    }

    public void Send_UDP(){
        handler.postDelayed(new Runnable() {

        public void run() {
            try {
                PuertoString = "8506"; //CASA DE DANIEL
                PUERTO = Integer.parseInt(PuertoString);
                PuertoString2 = "60000"; //CASA DE SANTIAGO
                PUERTO2 = Integer.parseInt(PuertoString2);
                PuertoString3 = "9000"; //CASA DE RICHARD
                PUERTO3 = Integer.parseInt(PuertoString3);
                IPaddress = InetAddress.getByName("gpstracker.sytes.net"); //CASA DE DANIEL
                IPaddress2 = InetAddress.getByName("ddnsdesign.ddns.net"); //CASA DE SANTIAGO
                IPaddress3 = InetAddress.getByName("gpstracker.hopto.me"); //CASA DE RICHARD
                Date = null;
                Date = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Localizacion.TimeVar);
                String Mensaje = (Localizacion.Late + "\n" + Localizacion.Longe + "\n" + Date);
                udp_Thread = new UDP_Thread(PUERTO, PUERTO2, PUERTO3, Mensaje, IPaddress, IPaddress2, IPaddress3);
                udp_Thread.start();

                if(Status) {
                    handler.postDelayed(this, delay);
                    Toast.makeText(MainActivity.this, "Enviando...", Toast.LENGTH_SHORT).show();
                } else {
                    handler.getLooper();
                }
             } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }}
                , delay);
    }
}



