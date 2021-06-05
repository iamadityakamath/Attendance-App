package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Employer_calender extends AppCompatActivity {
    TextView viewpdf,uploadpdf;
    CalendarView calender;
    String message;
    StorageReference storagerefrence;
    Uri imageuri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_calender);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.employer_calendar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.employer_Dashboad:
                        startActivity(new Intent(getApplicationContext(),Employer_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_add_user:
                        startActivity(new Intent(getApplicationContext(),Employer_addemployee.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_calendar:
                        return true;

                    case R.id.employer_Profile:
                        startActivity(new Intent(getApplicationContext(),Employer_profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_home:
                        startActivity(new Intent(getApplicationContext(),Employer_search.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        calender = (CalendarView )findViewById(R.id.employee_calendarView);
        viewpdf = findViewById(R.id.employer_view_pdf);
        uploadpdf = findViewById(R.id.employer_upload_pdf);
        uploadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 1);
            }
        });
    }
    ProgressDialog dialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading");
            dialog.show();
            imageuri = data.getData();
            final String timestamp = "" + System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final String messagePushID = timestamp;

            final StorageReference filepath = storageReference.child("HolidayList" + "." + "pdf");
            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        dialog.dismiss();
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();
                        Toast.makeText(Employer_calender.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(Employer_calender.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void employee_calendartoviewholidaypdf(View v){
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, View_holiday_pdf.class);
        startActivity(intent);
    }
}