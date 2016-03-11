package com.stanley.myapplication.StartActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.stanley.myapplication.Locations.SaveLocActivity;
import com.stanley.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.userid_tv);
        button = (Button) findViewById(R.id.sign_in_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = autoCompleteTextView.getText().toString();
                int input_int = Integer.parseInt(input);

                finish();
                Intent intent = new Intent(LoginActivity.this, SaveLocActivity.class);
                intent.putExtra("userId", input_int);
                startActivity(intent);

            }
        });

    }
}
