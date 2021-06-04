package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Employer_home extends AppCompatActivity {
    String userID;



    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseFirestore fref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        CollectionReference myref = fstore.collection("users").document(userID).collection("Daily");

    }




    ///Intent
    public void employerhome_to_addemployee(View v) {
        //Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_addemployee.class);
        startActivity(intent);
        finish();
    }
    public void employerhome_to_employer_profile(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_profile.class);
        startActivity(intent);
        finish();
    }
    public void employerhome_to_employer_calender(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_calender.class);
        startActivity(intent);
        finish();
    }
}