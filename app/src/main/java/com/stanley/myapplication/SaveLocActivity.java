package com.stanley.myapplication;

import android.content.ContentResolver;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

public class SaveLocActivity extends AppCompatActivity {

    private Button btn_add_loc;
    private ImageView logo;
    private ProgressBar pb;

    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_loc);

        btn_add_loc = (Button) findViewById(R.id.btn_add_loc);
        logo = (ImageView) findViewById(R.id.logo);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
    }

    public void saveLocations(View v) {
        if (displayGpsStatus()){
            pb.setVisibility(View.VISIBLE);
            Toast.makeText(getBaseContext(), "GPS ON", Toast.LENGTH_LONG).show();
            try {
                startActivityForResult(builder.build(this), 0);
                Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();
            }
            catch (GooglePlayServicesRepairableException e){
            }
            catch (GooglePlayServicesNotAvailableException e){
            }
        }
        else{
            Toast.makeText(getBaseContext(), "GPS NOT ON", Toast.LENGTH_LONG).show();
        }

    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }


}
