package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Employer_home extends AppCompatActivity implements View.OnClickListener{
    private CardView HomeCard, AddUserCard, CalendarCard, ProfileCard;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);
        fAuth = FirebaseAuth.getInstance();
        HomeCard = (CardView) findViewById(R.id.HomeCard);
        AddUserCard = (CardView) findViewById(R.id.AddUserCard);
        CalendarCard = (CardView) findViewById(R.id.CalendarCard);
        ProfileCard = (CardView) findViewById(R.id.ProfileCard);


        HomeCard.setOnClickListener(this);
        AddUserCard.setOnClickListener(this);
        CalendarCard.setOnClickListener(this);
        ProfileCard.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.HomeCard : i = new Intent(this, Employer_search.class);startActivity(i); break;
            case R.id.AddUserCard: i = new Intent(this, Employer_addemployee.class);startActivity(i); break;
            case R.id.CalendarCard: i = new Intent(this, Employer_calender.class);startActivity(i); break;
            case R.id.ProfileCard: i = new Intent(this, Employer_profile.class);startActivity(i); break;
            default:break ;
        }
    }
}