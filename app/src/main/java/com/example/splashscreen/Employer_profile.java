package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class Employer_profile extends AppCompatActivity {
    TextView fullname,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;
    String userID,pname,pemail,pphone;
    FirebaseUser user;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.employer_Profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.employer_Dashboad:
                        startActivity(new Intent(getApplicationContext(),Employer_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_add_user:
                        startActivity(new Intent(getApplicationContext(),Employer_addemployee.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_calendar:
                        startActivity(new Intent(getApplicationContext(),Employer_calender.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_Profile:
                        return true;

                    case R.id.employer_home:
                        startActivity(new Intent(getApplicationContext(),Employer_search.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        profileImage = findViewById(R.id.employer_profilepic);
        fullname = findViewById(R.id.Employer_profile_Name);
        email = findViewById(R.id.employer_profile_Email);
        phone = findViewById(R.id.employer_profile_Phoneno);

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
        DocumentReference documentReference = fstore.collection("Employer").document(userID);
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
    public void employer_profile_to_edit_profile(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_edit_profile.class);
        intent.putExtra("fullname",fullname.getText().toString());
        intent.putExtra("email",email.getText().toString());
        intent.putExtra("phone",phone.getText().toString());
        startActivity(intent); }

    public void employer_profile_to_login(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    public  void employer_Call(View v) {   Uri u = Uri.parse("tel: 9833662833");
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        try
        { startActivity(i); }
        catch (SecurityException s)
        {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show(); }
    }
    public void employer_gotdoc(View v){
        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
        openURL.setData(Uri.parse("http://www.google.com"));
        startActivity(openURL); }

}