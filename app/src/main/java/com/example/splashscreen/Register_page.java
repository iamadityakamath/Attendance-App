package com.example.splashscreen;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_page extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button button;
    EditText name, email, phonenum, password;
    ProgressBar progressbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    CheckBox employee_check,employer_check;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        button = findViewById(R.id.admin_add_regesterbutton);
        name = findViewById(R.id.admin_add_name);
        email = findViewById(R.id.admin_add_email);
        phonenum = findViewById(R.id.admin_add_phone);
        password = findViewById(R.id.admin_add_password);
        progressbar = findViewById(R.id.Regester_progressBar);
        employee_check = findViewById(R.id.regester_employee_checkBox);
        employer_check = findViewById(R.id.regester_employer_checkBox);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        /// check box logic
        employer_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                employee_check.setChecked(false);
            }
        });
        employee_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                employer_check.setChecked(false);
            }
        });

        // On clicking the register button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String fnmame = name.getText().toString();
                String phone = phonenum.getText().toString();

                if(employee_check.isChecked() || employer_check.isChecked()){
                    Toast.makeText(Register_page.this,"Select the account type",Toast.LENGTH_SHORT).show();
                }

                ///if any field is empty
                if((TextUtils.isEmpty(fnmame))){
                    name.setError("password is required");
                    return;
                }
                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email is required");
                    return;
                }
                if((TextUtils.isEmpty(phone))){
                    phonenum.setError("password is required");
                    return;
                }
                if((TextUtils.isEmpty(pass))){
                    password.setError("password is required");
                    return;
                }
                if(pass.length()<6) {
                    password.setError("password length is too short");
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);

                //Regester user in firebase
                fAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Register_page.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentrefrence = fstore.collection("Employer").document(userID);
                            Map<String,Object> userinfo = new HashMap<>();
                            userinfo.put("fname", fnmame);
                            userinfo.put("email",mail);
                            userinfo.put("phone",phone);

                            if(employee_check.isChecked()){
                                userinfo.put("isAdmin","1");
                            }
                            if(employer_check.isChecked()){
                                userinfo.put("isEmployer","1");
                            }

                            documentrefrence.set(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"on success: user profile is created for: "+ userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), login_page.class));
                        }
                        else {
                            Toast.makeText(Register_page.this, "Error ! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
    public void registertologin(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, login_page.class);
        startActivity(intent);
        finish();

    }



}

