package com.stanley.myapplication.Locations;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.stanley.myapplication.R;

import java.util.Date;

public class SaveLocActivity extends AppCompatActivity {
    private static final String TAG = "SaveLocActivity";

    private static int REQUEST_PLACE_PICKER=1;

    private Button btn_add_loc;

    private ListView listView;

    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    private MySQLiteLocHelper mySQLiteLocHelper;
    private int userId = 1;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_loc);

        btn_add_loc = (Button) findViewById(R.id.btn_add_loc);

        populateListView();
        registerClickItemCallBack();
    }

    public void saveLocations(View v) {
        if (displayGpsStatus()){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final double la = place.getLatLng().latitude;
            final double lon = place.getLatLng().longitude;
            final CharSequence address = place.getAddress();
            final String placeId = place.getId();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            Log.d(TAG, "la: " + la + " lon: " + lon);
            mySQLiteLocHelper = new MySQLiteLocHelper(SaveLocActivity.this);
            Log.d(TAG,""+new Date().getTime());
            int res = mySQLiteLocHelper.specialLocInsert(userId, la, lon, new Date().getTime(), 1);
            Log.d(TAG, "retrun: " + res);
//
//            mViewName.setText(name);
//            mViewAddress.setText(address);
//            mViewAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void populateListView(){
        //create list item
        String[] str = {"sd", "sdf"};

        //adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, str);

        //configure the listview
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void registerClickItemCallBack(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) view;
                String message = "Do you want to delete this location?";


            }
        });
    }

}
