package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splash extends AppCompatActivity {
    Button button;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        button = findViewById(R.id.login_button);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        if(fAuth.getCurrentUser() != null) {
            userID = fAuth.getCurrentUser().getUid();

            DocumentReference documentrefrence1 = fstore.collection("users").document(userID);
            documentrefrence1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG","onsucess"+documentSnapshot.getData());
                    if (documentSnapshot.getString("isUser") != null){
                        startActivity(new Intent(getApplicationContext(),employee_home.class));
                        finish();
                    };
                }
            });

            DocumentReference documentrefrence2 = fstore.collection("Admin").document(userID);
            documentrefrence2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG","onsucess"+documentSnapshot.getData());

                    if (documentSnapshot.getString("isAdmin") != null){
                        startActivity(new Intent(getApplicationContext(),Admin_home.class));
                        finish();
                    };
                }
            });

            DocumentReference documentrefrence3 = fstore.collection("Employer").document(userID);
            documentrefrence3.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG","onsucess"+documentSnapshot.getData());
                    if (documentSnapshot.getString("isEmployer") != null){
                        startActivity(new Intent(getApplicationContext(), Employer_home.class));
                        finish();
                    };
                }
            });
        }





    }
    public void openloginpage(View v){
        Toast.makeText(this, "Kindly Login", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, login_page.class);
        startActivity(intent);
        finish();
    }
}