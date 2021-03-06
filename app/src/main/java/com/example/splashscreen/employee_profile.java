package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class employee_profile extends AppCompatActivity {
    TextView fullname,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;
    String userID;
    FirebaseUser user;
    ImageView profileImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation2);
        bottomNavigationView.setSelectedItemId(R.id.employee_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.employee_home:
                        startActivity(new Intent(getApplicationContext(),employee_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employee_profile:
                        return true;

                    case R.id.employee_calendar:
                        startActivity(new Intent(getApplicationContext(),employee_calendar.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employee_signout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),login_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(0,0);
                        finish();
                }
                return false;
            }
        });


        profileImage = findViewById(R.id.employee_profilepic);
        fullname = findViewById(R.id.Employee_profile_Name);
        email = findViewById(R.id.employee_profile_Email);
        phone = findViewById(R.id.employee_profile_Phoneno);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storagerefrence = FirebaseStorage.getInstance().getReference();

        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        StorageReference profileRef = storagerefrence.child("users/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(profileImage);
            }
        });

        ///Access userInfo from firebase
        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                fullname.setText(documentSnapshot.getString("fname"));
                email.setText(documentSnapshot.getString("email"));
                phone.setText(documentSnapshot.getString("phone"));

            }

        });




    }


    /// Onclickbuttons for bottom navigationbar
    public void employee_profile_to_edit_profile(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_edit_profile.class);
        intent.putExtra("fullname",fullname.getText().toString());
        intent.putExtra("email",email.getText().toString());
        intent.putExtra("phone",phone.getText().toString());
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void employee_profile_to_login(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(0,0);
        finish();
    }
    public  void employee_Call(View v) {   Uri u = Uri.parse("tel: 12345678");
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        try
        { startActivity(i); }
        catch (SecurityException s)
        {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show(); }
    }
    public void employee_gotdoc(View v){
        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
        openURL.setData(Uri.parse("http://www.google.com"));
        startActivity(openURL); }

}