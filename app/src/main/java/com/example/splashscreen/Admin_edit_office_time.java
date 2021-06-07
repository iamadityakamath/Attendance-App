package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Admin_edit_office_time extends AppCompatActivity {
    TextView Office_start,Office_stop,max_hours;
    Button Save_admin_offtime;
    CharSequence gethours;
    int minutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_office_time);
        Office_start = findViewById(R.id.edit_office_start);
        Office_stop = findViewById(R.id.edit_office_stop);
        max_hours = findViewById(R.id.edit_office_maxtime);
        Save_admin_offtime = findViewById(R.id.Save_admin_offtime);

//        Office_start =preg_match("/^(?:2[0-3]|[01][0-9]):[0-5][0-9]$/", $foo);
        gethours = max_hours.getText().toString();
    }

}