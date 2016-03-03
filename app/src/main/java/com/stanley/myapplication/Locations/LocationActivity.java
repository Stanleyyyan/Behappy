package com.stanley.myapplication.Locations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stanley.myapplication.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LocationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationChangeListener{
    private static final String TAG = "LocationActivity";

    private static final int MAP_ZOOM = 18; // Google Maps supports 1-21

    private GoogleMap mMap;

    private LocationManager locationManager;
    private Location currentLocation;
    private Location preLocation;//previous location
    private Location startLocation;

    private LatLng startLatLng;
    private LatLng endLatLng;
    private LatLng currentLatLng;

    private ToggleButton tb;

    private Boolean tracking;

    private long distance;
    private long startTime;

    private double maxRange;

    private Date endDate;
    private Date startDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

    private MySQLiteLocHelper mySQLiteLocHelper;

    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {

            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationChangeListener(this);

                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setMap();
                }
            }

            ToggleButton trackingToggleButton = (ToggleButton) findViewById(R.id.trackingToggleButton);
            distance = 0;
            tracking = false;
            maxRange = 0;

            trackingToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        }
    }

    public void setMap() {

        if (mMap != null) {
            getMyCurrentLocation();
        }

        if (currentLocation != null) {
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLatLng)
                    .zoom(MAP_ZOOM)
                    .bearing(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } else {
            //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(0, 0))
                    .zoom(MAP_ZOOM)
                    .bearing(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

    }

    public void getMyCurrentLocation() {

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this.getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // Finds a provider that matches the criteria
        String provider = locationManager.getBestProvider(criteria, true);
        // Use the provider to get the last known location

        locationManager.requestLocationUpdates(provider, 10000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, location.toString());

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

    CompoundButton.OnCheckedChangeListener toggleButtonListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (!isChecked) {//it is not checked
                tracking = false;
                startLocation = null;
                startLatLng = null;
                endLatLng = currentLatLng;
                endDate = new Date();

                mMap.addMarker(new MarkerOptions().position(endLatLng).title("END"));

                // create a dialog displaying the results
//                AlertDialog.Builder dialogBuilder =
//                        new AlertDialog.Builder(LocationActivity.this);
//                dialogBuilder.setTitle("RESULT");

                double distanceKM = distance / 1000.0;
                double maxRangeKM = maxRange / 1000.0;

                long diff = endDate.getTime() - startDate.getTime();
                long diffSeconds = diff / 1000 % 60;

//                long diffMinutes = diff / (60 * 1000) % 60;
//                long diffHours = diff / (60 * 60 * 1000) % 24;
//                long diffDays = diff / (24 * 60 * 60 * 1000);

                // display distanceTraveled traveled and average speed
//                dialogBuilder.setMessage("DISTANCE: " + distanceKM + " km"
//                        + "\nRANGE: " + String.format("%.2f", maxRange)  + "km"
//                        + "\nStart DATE: " + startDate
//                        + "\nEnd Date: " + endDate + "\nTime DIFF: " + diffSeconds);
//
//                dialogBuilder.setPositiveButton(
//                        "OK", null);
//
//                dialogBuilder.show(); // display the dialog

                mySQLiteLocHelper = new MySQLiteLocHelper(LocationActivity.this);
                mySQLiteLocHelper.insertLoc(userId, startDate.getTime(), distanceKM, maxRangeKM, diff);


            } else {
                mMap.clear();
                distance = 0;
                maxRange = 0;
                startDate = new Date();

                //start tracking
                startLocation = currentLocation;
                currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                startLatLng = currentLatLng;//store start location
                tracking = true;
                startTime = System.currentTimeMillis(); // get current time

                mMap.addMarker(new MarkerOptions().position(startLatLng).title("START"));

                endLatLng = null; // starting a new route


            }
        }
    };


    @Override
    public void onMyLocationChange(Location location) {

        preLocation = currentLocation;
        currentLocation = location;

        currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentLatLng)
                .zoom(MAP_ZOOM)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (tracking) {

            maxRange = Math.max(maxRange,currentLocation.distanceTo(startLocation));

            distance = distance + (long) preLocation.distanceTo(currentLocation);


            //mMap.addMarker(new MarkerOptions().position(currentLatLng).title(currentLatLng.toString()));

            cameraPosition = new CameraPosition.Builder()
                    .target(currentLatLng)
                    .zoom(MAP_ZOOM)
                    .bearing(0)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }
}
