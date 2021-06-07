package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Admin_home extends AppCompatActivity implements View.OnClickListener {

    private CardView aHomeCard, aAddUserCard, aCalendarCard, aProfileCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        aHomeCard = (CardView) findViewById(R.id.aHomeCard);
        aAddUserCard = (CardView) findViewById(R.id.aAddUserCard);
        aCalendarCard = (CardView) findViewById(R.id.aCalendarCard);
        aProfileCard = (CardView) findViewById(R.id.aProfileCard);


        aHomeCard.setOnClickListener(this);
        aAddUserCard.setOnClickListener(this);
        aCalendarCard.setOnClickListener(this);
        aProfileCard.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.aHomeCard : i = new Intent(this, Admin_Search.class);startActivity(i); break;
            case R.id.aAddUserCard: i = new Intent(this, Admin_addEmployer.class);startActivity(i); break;
            case R.id.aCalendarCard: i = new Intent(this, Admin_Calendar.class);startActivity(i); break;
            case R.id.aProfileCard: i = new Intent(this, Admin_Profile.class);startActivity(i); break;
            default:break ;

        }

    }
    public void chenge_off_time(View v){
        Intent intent = new Intent(this, Admin_edit_office_time.class);
        startActivity(intent);
    }
}
