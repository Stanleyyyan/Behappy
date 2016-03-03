package com.stanley.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.androidadvance.androidsurvey.SurveyActivity;
import com.stanley.myapplication.Locations.LocationActivity;
import com.stanley.myapplication.Locations.MySQLiteLocHelper;
import com.stanley.myapplication.Locations.SaveLocActivity;
import com.stanley.myapplication.contactlist.ContactActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Main2Activity";

    private static final int SURVEY_REQUEST = 1337;
    private Button btn_lonely;

    private int userId = 1;

    private LocationManager mlocManager;
    private Location currentLoc;
    MySQLiteLocHelper mySQLiteLocHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            LocationListener mlocListener = new MyLocationListener();
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mlocListener);

        } catch (SecurityException e) {

        }

        btn_lonely = (Button) findViewById(R.id.btn_lonely);
        btn_lonely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lon = 0.0;
                double la = 0.0;

                if (currentLoc != null) {
                    la = currentLoc.getLatitude();
                    lon = currentLoc.getLongitude();
                    Log.d(TAG, "la" + la + "lon" + lon);

                    mySQLiteLocHelper = new MySQLiteLocHelper(Main2Activity.this);
                    mySQLiteLocHelper.specialLocAddLonely(userId, la, lon, new Date().getTime(), 0);
                }

                Intent intent = new Intent(Main2Activity.this, AppUsageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_loc) {
            Intent intent = new Intent(Main2Activity.this, SaveLocActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_survey) {
            Intent i_survey = new Intent(Main2Activity.this, SurveyActivity.class);
            i_survey.putExtra("json_survey", loadSurveyJson("example_survey_1.json"));
            startActivityForResult(i_survey, SURVEY_REQUEST);

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(Main2Activity.this, ContactActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_app_use) {
            Intent intent = new Intent(Main2Activity.this, AppUsageActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(Main2Activity.this, LocationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_show_contacts) {
            Intent intent = new Intent(Main2Activity.this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_delete) {
            mySQLiteLocHelper = new MySQLiteLocHelper(Main2Activity.this);
            mySQLiteLocHelper.deleteAll(Main2Activity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String loadSurveyJson(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Date date = new Date();
        if (requestCode == SURVEY_REQUEST) {
            if (resultCode == RESULT_OK) {

                String answers_json = data.getExtras().getString("answers");

                Log.d("****", "****************** WE HAVE ANSWERS ******************");
                Log.v("ANSWERS JSON", answers_json);
                Log.d("****", "*****************************************************");

                String[] str_result = answers_json.split(",");
                Log.d("answers", str_result[0]);

                String answerStr = "";

                for (int i = 0; i < str_result.length; i++) {
                    String[] substr = str_result[i].split(":");
                    char answer = substr[1].charAt(1);
                    answerStr = answerStr + answer;
                    Log.d("final answer", ""+answer);
                }

                mySQLiteLocHelper = new MySQLiteLocHelper(Main2Activity.this);
                int res = mySQLiteLocHelper.surveyInsert(userId, date.getTime() , answerStr);
            }
        }
    }

    public class MyLocationListener implements LocationListener{
        @Override

        public void onLocationChanged(Location loc){
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
