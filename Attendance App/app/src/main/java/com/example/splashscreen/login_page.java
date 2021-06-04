package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_page extends AppCompatActivity {
    TextView textview, forgetpass;
    EditText l_pass, l_email;
    Button loginbt;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        l_email = findViewById(R.id.login_email);
        l_pass = findViewById(R.id.login_Password);
        progressBar = findViewById(R.id.login_progressBar);
        loginbt = findViewById(R.id.login_button);
        forgetpass = findViewById(R.id.login_forgetpassword);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();



        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = l_email.getText().toString().trim();
                String pass = l_pass.getText().toString().trim();

                //Check input conditions
                if (TextUtils.isEmpty(mail)) {
                    l_email.setError("Email is required");
                    return;
                }
                if((TextUtils.isEmpty(pass))){
                    l_pass.setError("password is required");
                    return;
                }
                if(pass.length()<6) {
                    l_pass.setError("password length is too short");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                fAuth.signInWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //Toast.makeText(login_page.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        CheckUserAccessLevel(userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_page.this, "Error ! "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });


            }
        });

        // Forget Pasword
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog =new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your mail to get reset link ");
                passwordResetDialog.setView(resetmail);

                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and setreset link
                        String rmail = resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(rmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login_page.this, "reset sucessfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login_page.this, "Error!  Reset link not sent: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialouge

                    }
                });
                passwordResetDialog.create().show();

            }
        });
    }

    public void Registerpage(View v) {
        Toast.makeText(this, "Kindly Register", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Register_page.class);
        startActivity(intent);

    }

    //Check user access level
    public void CheckUserAccessLevel(String userID) {

        DocumentReference documentrefrence = fstore.collection("users").document(userID);
        documentrefrence.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onsucess"+documentSnapshot.getData());
                if (documentSnapshot.getString("isUser") != null){
                    startActivity(new Intent(getApplicationContext(),employee_home.class));
                    finish();
                };
                if (documentSnapshot.getString("isAdmin") != null){
                    startActivity(new Intent(getApplicationContext(),Admin_home.class));
                    finish();
                };
                if (documentSnapshot.getString("isEmployer") != null){
                    startActivity(new Intent(getApplicationContext(), Employer_home.class));
                    finish();
                };
            }
        });

    }
}
