package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class employee_edit_profile extends AppCompatActivity {
    EditText ename,ephone,eemail;
    FirebaseAuth fAuth;
    Button Save_button;
    ImageView change_profile_img;
    FirebaseFirestore fstore;
    FirebaseUser user;
    StorageReference storagerefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit_profile);

        Intent data = getIntent();
        String fullname = data.getStringExtra("fullname");
        String email = data.getStringExtra("email");
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

        user = fAuth.getCurrentUser();
        Log.d("TAG","On Create " + fullname+" "+email+ " "+phone);

        ename = findViewById(R.id.empoyee_edit_profile_name);
        ephone = findViewById(R.id.empoyee_edit_profile_phone);
        eemail = findViewById(R.id.empoyee_edit_profile_email);
        Save_button = findViewById(R.id.employee_save_edit_profile_button);
        change_profile_img = findViewById(R.id.wmployee_edit_profilepic);

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
                 if(ename.getText().toString().isEmpty() || ephone.getText().toString().isEmpty() || eemail.getText().toString().isEmpty()){
                     Toast.makeText(employee_edit_profile.this,"One or many fields are empty.",Toast.LENGTH_SHORT).show();
                     return;
                 }
                 final String femail = eemail.getText().toString();
                 user.updateEmail(femail).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         DocumentReference docref = fstore.collection("users").document(user.getUid());
                         Map<String,Object> edited = new HashMap<>();
                         edited.put("fname",ename.getText().toString());
                         edited.put("email",femail);
                         edited.put("phone",ephone.getText().toString());
                         docref.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(employee_edit_profile.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(getApplicationContext(),employee_home.class));
                                 finish();
                             }
                         });
                         //Toast.makeText(employee_edit_profile.this,"Email is changed",Toast.LENGTH_SHORT).show();
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(employee_edit_profile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                     }
                 });

            }
        });

        ename.setText(fullname);
        eemail.setText(email);
        ephone.setText(phone);

    }
    public void employee_edit_profile_to_profile(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_profile.class);
        startActivity(intent); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imguri = data.getData();
                //change_profile_img.setImageURI(imguri);
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
}