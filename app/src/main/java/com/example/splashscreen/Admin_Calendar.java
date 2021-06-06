package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Admin_Calendar extends AppCompatActivity {
    TextView viewpdf,uploadpdf;
    CalendarView calender;
    String message;
    StorageReference storagerefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__calendar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation3);
        bottomNavigationView.setSelectedItemId(R.id.admin_calendar_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.admin_Dashboad_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.admin_add_user_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_addEmployer.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.admin_search_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_Search.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.admin_calendar_nav:
                        return true;

                    case R.id.admin_Profile_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        calender = (CalendarView )findViewById(R.id.aemployee_calendarView);
        viewpdf = findViewById(R.id.aemployer_view_pdf);

    }


    public void admin_calendartoviewholidaypdf(View v){
        Intent intent = new Intent(this, View_holiday_pdf.class);
        overridePendingTransition(0,0);
        startActivity(intent);
    }

}