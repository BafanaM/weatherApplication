package com.bafana.weatherapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import services.LocationService;
import services.NetworkService;

/**
 * Created by bafanamankahla on 2017/01/16.
 */

public class MainActivity extends AppCompatActivity {

    public static TextView tvCity;
    public static TextView tvTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvCity = (TextView) findViewById(R.id.tv_city_name);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);

        locationSettings();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSettings();
            }
        });
    }

    private void locationSettings() {

        LocationService gps = new LocationService(MainActivity.this);

        double lat;
        double lng;
        if (gps.canGetLocation()) {

            lng = gps.getLongitude();
            lat = gps.getLatitude();

            NetworkService task = new NetworkService(this);
            task.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(lat) + "&lon=" + String.valueOf(lng) + "&appid=ebb827e84b4656c6af3f8d359887dcf6");
        } else {
            gps.showSettingsAlert();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
