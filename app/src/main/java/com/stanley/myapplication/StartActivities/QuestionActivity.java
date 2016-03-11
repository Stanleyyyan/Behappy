package com.stanley.myapplication.StartActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.stanley.myapplication.Locations.SaveLocActivity;
import com.stanley.myapplication.R;

public class QuestionActivity extends AppCompatActivity {

//    private RadioButton radioButton_yes;
//    private RadioButton radioButton_no;
    private Boolean answer;

    private Button button;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            userId = 1;
        } else {
            userId = extras.getInt("userId");
        }

//        radioButton_no = (RadioButton) findViewById(R.id.radioButton_no);
//        radioButton_yes = (RadioButton) findViewById(R.id.radioButton_yes);

        button = (Button) findViewById(R.id.next_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(QuestionActivity.this, SaveLocActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("livealong", answer);
                startActivity(intent);

            }
        });
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_yes:
                if (checked)
                    answer = true;
                    break;
            case R.id.radioButton_no:
                if (checked)
                    answer = false;
                    break;
        }
    }
}
