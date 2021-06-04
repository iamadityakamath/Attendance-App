package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class employee_home extends AppCompatActivity {
    ImageView employee_home_img;
    FirebaseUser user;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        employee_home_img = findViewById(R.id.employee_home_profilepic);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storagerefrence = FirebaseStorage.getInstance().getReference();

        user = fAuth.getCurrentUser();

        StorageReference profileRef = storagerefrence.child("users/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(employee_home_img);
            }
        });
    }
    public void employee_Checkinoutpg(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_checkinout.class);
        startActivity(intent);
    }

    public void employee_hometocalendar(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_calendar.class);
        startActivity(intent);
    }

    public void employee_home_to_profile(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_profile.class);
        startActivity(intent);
    }
}