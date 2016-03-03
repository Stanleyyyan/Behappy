package com.stanley.myapplication.Locations;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Location {
    private double latitude;
    private double longitude;
    private LocationManager mlocManager;
    private android.location.Location currentLoc;
    private Context context;

    private static final String TAG = "Location";

    public Location(Context context){
        this.context = context;

        try {
            mlocManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            LocationListener mlocListener = new MyLocationListener();
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mlocListener);

        } catch (SecurityException e) {

        }
    }

    protected void showCurrentLocation() {



        //currentLoc = locationManager.getLastKnownLocation(provider);

    }

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(android.location.Location location) {
            Log.d("Location", ""+location.toString());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public class MyLocationListener implements LocationListener{
        @Override

        public void onLocationChanged(android.location.Location loc){
            try {
                currentLoc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.d(TAG, "" + currentLoc.getLatitude());
            } catch (SecurityException e) {

            }

        }
        public void onProviderDisabled(String provider){

            //nothing
        }


        public void onProviderEnabled(String provider){

            //nothing
        }

        public void onStatusChanged(String provider, int status, Bundle extras){
            //nothing
        }
    }/* End of Class MyLocationListener */


}
