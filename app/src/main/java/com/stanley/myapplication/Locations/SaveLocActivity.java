package com.stanley.myapplication.Locations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.stanley.myapplication.Main2Activity;
import com.stanley.myapplication.MySQLiteLocHelper;
import com.stanley.myapplication.R;
import com.stanley.myapplication.contactlist.ContactActivity;

public class SaveLocActivity extends AppCompatActivity {
    private static final String TAG = "SaveLocActivity";

    private static int REQUEST_PLACE_PICKER = 1;

    private Button btn_add_loc;
    private Button btn_save;

    private ListView listView;

    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    private MySQLiteLocHelper mySQLiteLocHelper;
    private int userId;

    private LocationBehappy locationBehappy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_loc);
        locationBehappy = new LocationBehappy();
        locationBehappy.setType(0);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            userId = 1;
        } else {
            userId = extras.getInt("userId");
        }

        btn_add_loc = (Button) findViewById(R.id.btn_add_loc);

        populateListView();
        registerClickItemCallBack();

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationBehappy.getType() == 0) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaveLocActivity.this);
                    dialogBuilder.setTitle("WARNING");
                    dialogBuilder.setMessage("Please choose a type for this location");
                    dialogBuilder.setPositiveButton("OK", null);
                    dialogBuilder.show(); // display the dialog
                } else {
                    mySQLiteLocHelper = new MySQLiteLocHelper(SaveLocActivity.this);
                    int res = mySQLiteLocHelper.specialLocInsert(userId, locationBehappy);
                    Log.d(TAG, "retrun: " + res);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SaveLocActivity.this);
                    builder.setMessage("SUCCESS! Do you want to add another one? ").setPositiveButton("YES", dialogClickListener)
                            .setNegativeButton("NO", dialogClickListener).show();
                }

            }
        });


    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    finish();
                    Intent intent2 = new Intent(SaveLocActivity.this, ContactActivity.class);
                    //Intent intent2 = new Intent(SaveLocActivity.this, Main2Activity.class);
                    intent2.putExtra("userId", userId);
                    startActivity(intent2);
                    break;
            }

        }
    };

    public void saveLocations(View v) {
        if (displayGpsStatus()) {
            Toast.makeText(getBaseContext(), "GPS ON", Toast.LENGTH_LONG).show();
            try {
                startActivityForResult(builder.build(this), 0);
                //Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();
            } catch (GooglePlayServicesRepairableException e) {
            } catch (GooglePlayServicesNotAvailableException e) {
            }
        } else {
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

            locationBehappy.setLatitude(la);
            locationBehappy.setLongitude(lon);
            Log.d(TAG, "la: " + la + " lon: " + lon);
//
//
//            mViewName.setText(name);
//            mViewAddress.setText(address);
//            mViewAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void populateListView() {
        //create list item
        String[] str = {"where I live", "where my family lives", "where I meet my friends", "where I learn"};

        //adapter
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, str);
        ArrayAdapter<String> adapter = new TypeApater(this, str);


        //configure the listview
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void registerClickItemCallBack() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        locationBehappy.setType(1);
                        break;
                    case 1:
                        locationBehappy.setType(2);
                        break;
                    case 2:
                        locationBehappy.setType(3);
                        break;
                    case 3:
                        locationBehappy.setType(4);
                        break;
                    default:
                        locationBehappy.setType(0);
                        break;
                }
            }
        });
    }

}
