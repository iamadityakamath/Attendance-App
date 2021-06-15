package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class employee_edit_profile extends AppCompatActivity {
    EditText ename,ephone;
    TextView epass;
    FirebaseAuth fAuth;
    Button Save_button;
    String email;
    String userID,Employer_UserID;
    ImageView change_profile_img;
    FirebaseFirestore fstore;
    String user;
    StorageReference storagerefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit_profile);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation2);
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
                        startActivity(new Intent(getApplicationContext(),employee_profile.class));
                        overridePendingTransition(0,0);
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

        ename = findViewById(R.id.empoyee_edit_profile_name);
        ephone = findViewById(R.id.empoyee_edit_profile_phoneno);
        epass = findViewById(R.id.empoyee_edit_profile_password);
        Save_button = findViewById(R.id.employee_save_edit_profile_button);
        change_profile_img = findViewById(R.id.wmployee_edit_profilepic);

        Intent data = getIntent();
        String fullname = data.getStringExtra("fullname");
        email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storagerefrence = FirebaseStorage.getInstance().getReference();




        StorageReference profileRef = storagerefrence.child("users/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(change_profile_img);
            }
        });




        //Change Profile picture
        change_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(employee_edit_profile.this, "Profile image clicked", Toast.LENGTH_SHORT).show();
                Intent openGalleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryintent,1000);
            }
        });


        /// Save the email and edit other parameters
        Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ename.getText().toString().isEmpty() || ephone.getText().toString().isEmpty()) {
                    Toast.makeText(employee_edit_profile.this, "One or many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!ephone.getText().toString().matches("\\d{10}")){
                    Toast.makeText(employee_edit_profile.this, "Phone no. is invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fAuth.getCurrentUser() != null) {
                    userID = fAuth.getCurrentUser().getUid();

                    DocumentReference documentrefrence1 = fstore.collection("users").document(userID);
                    documentrefrence1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("TAG", "onsucess" + documentSnapshot.getData());
                            if (documentSnapshot.getString("isUser") != null) {
                                Map<String, Object> edited = new HashMap<>();
                                edited.put("fname", ename.getText().toString());
                                edited.put("phone", ephone.getText().toString());
                                Employer_UserID = documentSnapshot.getString("Employer_UserID");
                                documentrefrence1.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(employee_edit_profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(employee_edit_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        ename.setText(fullname);
        ephone.setText(phone);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imguri = data.getData();
                uploadImageToFirebase(imguri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imguri) {
        //Upload Image to Firebse Firestore
        StorageReference fileRef = storagerefrence.child("users/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");
        fileRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(employee_edit_profile.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).transform(new CircleTransform()).into(change_profile_img);
                    }

                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(employee_edit_profile.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void reset_employeee_pass(View v){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   Toast.makeText(employee_edit_profile.this, "Password Reset Email Sent!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(employee_edit_profile.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
