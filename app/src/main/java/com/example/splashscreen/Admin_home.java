package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class Admin_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void Admin_profile_to_login(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login_page.class));
        finish();
    }

    public void admin_to_adminaddemployee(View v) {
        //Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Admin_addEmployer.class);
        startActivity(intent);
        finish();
    }
//    public void employeraddemployee_to_employer_profile(View v) {
//        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, Employer_profile.class);
//        startActivity(intent);
//        finish();
//    }
}