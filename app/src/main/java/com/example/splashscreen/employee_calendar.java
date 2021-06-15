package com.example.splashscreen;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class employee_calendar extends AppCompatActivity{

    TextView viewpdf;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_calendar);
        viewpdf = findViewById(R.id.employee_view_pdf);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation2);
        bottomNavigationView.setSelectedItemId(R.id.employee_calendar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.employee_home:
                        startActivity(new Intent(getApplicationContext(),employee_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employee_profile:
                        startActivity(new Intent(getApplicationContext(),employee_profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employee_calendar:
                        return true;

                    case R.id.employee_signout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),login_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(0,0);
                        finish();
                }
                return false;
            }
        });
    }

    public void employee_calendartoviewholidaypdf(View v){
        Intent intent = new Intent(this, View_holiday_pdf.class);
        startActivity(intent);
    }

}