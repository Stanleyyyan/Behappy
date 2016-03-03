package com.stanley.myapplication.Locations;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class LocationRecord {
    private LocationManager locationManager;
    private Location startLocation;
    private Location endLocation;
    private Location currentLocation;

    public LocationRecord(Context context){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // Finds a provider that matches the criteria
        String provider = locationManager.getBestProvider(criteria, true);
        // Use the provider to get the last known location

        locationManager.requestLocationUpdates(provider, 10000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        currentLocation = locationManager.getLastKnownLocation(provider);

    }


}
