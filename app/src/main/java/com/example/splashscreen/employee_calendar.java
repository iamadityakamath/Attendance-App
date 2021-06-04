package com.example.splashscreen;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class employee_calendar extends AppCompatActivity{

    TextView viewpdf;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_calendar);
        viewpdf = findViewById(R.id.employee_view_pdf);
    }

    public void employee_calendartohome(View v) {
        Intent intent = new Intent(this, employee_home.class);
        startActivity(intent);
    }
    public void employee_calendartoprofile(View v){
        Intent intent = new Intent(this, employee_profile.class);
        startActivity(intent);
    }
    public void employee_calendartoviewholidaypdf(View v){
        Intent intent = new Intent(this, View_holiday_pdf.class);
        startActivity(intent);
    }

}