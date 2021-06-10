package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.DateTime;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Admin_edit_office_time extends AppCompatActivity {
    TextView Office_start,Office_stop,max_hours;
    String OfficeStartTime,OfficeStopTime ;
    Double maxMinutes ;
    String db_start,db_stop;
    Double db_max;
    Button Save_admin_offtime;
    CharSequence gethours;
    int minutes;
    Format f = new SimpleDateFormat("HH.mm.ss");

    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_office_time);


        Office_start = findViewById(R.id.edit_office_start);
        Office_stop = findViewById(R.id.edit_office_stop);
        max_hours = findViewById(R.id.edit_office_maxtime);
        Save_admin_offtime = findViewById(R.id.Save_admin_offtime);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference db2 = fstore.collection("Admin").document("FGWUYBcerxMb456ecwuIxvbQJ8L2");
        db2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                OfficeStartTime = documentSnapshot.getString("OfficeStartTime");
                OfficeStopTime = documentSnapshot.getString("OfficeStopTime");
                maxMinutes = documentSnapshot.getDouble("maxMinutes");
                Office_start.setText(OfficeStartTime);
                Office_stop.setText(OfficeStopTime);
                max_hours.setText(maxMinutes.toString());
            }

        });


    }

    public void Save_admin_offtime_in_database(View view){
        db_start = Office_start.getText().toString();
        db_stop = Office_stop.getText().toString();
        if(max_hours.getText().toString().isEmpty()){
            max_hours.setError("minutes cannot be null");
            return;
        }
        if(!max_hours.getText().toString().isEmpty()){
            db_max =  Double.parseDouble(max_hours.getText().toString());
        }

        Log.d("ecdew_new", "db_max "+db_max);
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(db_start);
        Matcher n = p.matcher(db_stop);
        if(!m.matches()){
            Office_start.setError("kindly enter the time in HH:MM:SS format");
            return;
        }
        if(!n.matches()){
            Office_stop.setError("kindly enter the time in HH:MM:SS format");
            return;
        }


        DocumentReference db2 = fstore.collection("Admin").document("FGWUYBcerxMb456ecwuIxvbQJ8L2");
        db2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> userinfo = new HashMap<>();
                userinfo.put("OfficeStartTime",db_start);
                userinfo.put("OfficeStopTime",db_stop);
                userinfo.put("maxMinutes",db_max);
                db2.set(userinfo, SetOptions.merge());
                Log.d("ecdew_new", ""+db_start);
                Log.d("ecdew_new", ""+db_stop);
                Log.d("ecdew_new", ""+db_max);

            }
        });
        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Admin_home.class));
    }


}