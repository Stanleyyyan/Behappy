/*
* Copyright 2015 AndroidAdvance.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
* */

package com.stanley.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidadvance.androidsurvey.SurveyActivity;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int SURVEY_REQUEST = 1337;

    private Button btn_to_map;
    private Button btn_app_usage;
    private Button btn_add_loc;
    private Button btn_take_survey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_to_map = (Button) findViewById(R.id.btn_to_map);
        btn_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        btn_app_usage = (Button) findViewById(R.id.btn_app_usage);
        btn_app_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppUsageActivity.class);
                startActivity(intent);
            }
        });

        btn_add_loc = (Button) findViewById(R.id.btn_to_add_loc);
        btn_add_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveLocActivity.class);
                startActivity(intent);
            }
        });

        btn_take_survey = (Button) findViewById(R.id.btn_take_survey);
        btn_take_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_survey = new Intent(MainActivity.this, SurveyActivity.class);
                i_survey.putExtra("json_survey", loadSurveyJson("example_survey_1.json"));
                startActivityForResult(i_survey, SURVEY_REQUEST);

            }
        });



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



}

