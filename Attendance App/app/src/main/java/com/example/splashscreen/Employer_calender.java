package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_calender);

        calender = (CalendarView )findViewById(R.id.employee_calendarView);
        viewpdf = findViewById(R.id.employer_view_pdf);
        uploadpdf = findViewById(R.id.employer_upload_pdf);


    viewpdf.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            download();
        }
    });
    }
    public void download(){
        storagerefrence = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storagerefrence.child("HolidayList.pdf");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadfile(Employer_calender.this,"HolidayList",".pdf",DIRECTORY_DOWNLOADS,url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void downloadfile(Context context, String fileName, String FileExtension, String destinationDirectory, String url){
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+FileExtension);
        downloadmanager.enqueue(request);
    }

    public void employercalender_to_employer_home(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_home.class);
        startActivity(intent);
        finish();
    }
    public void employercalender_to_employer_addeployee(View v) {
        //Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_addemployee.class);
        startActivity(intent);
        finish();
    }
    public void employercalender_to_employer_profile(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_profile.class);
        startActivity(intent);
        finish();
    }
}