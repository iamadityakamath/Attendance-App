package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Admin_edit_office_time extends AppCompatActivity {
    TextView Office_start,Office_stop,max_hours;
    Button Save_admin_offtime;
    CharSequence gethours;
    int minutes;

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

//        Office_start =preg_match("/^(?:2[0-3]|[01][0-9]):[0-5][0-9]$/", $foo);
        gethours = max_hours.getText().toString();
        DocumentReference df = fstore.collection("Admin").document(userID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        });
    }

}