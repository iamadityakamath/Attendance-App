package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Employer_addemployee extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button button;
    EditText name, email, phonenum, passwordinp;
    String currentemail,currentpass;
    ProgressBar progressbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID1,userID;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_addemployee);
        button = findViewById(R.id.admin_add_regesterbutton);
        name = findViewById(R.id.admin_add_name);
        email = findViewById(R.id.admin_add_email);
        phonenum = findViewById(R.id.admin_add_phone);
        passwordinp = findViewById(R.id.admin_add_password);
        progressbar = findViewById(R.id.Regester_progressBar);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference df = fstore.collection("Employer").document(userID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentemail = documentSnapshot.getString("email");
                currentpass = documentSnapshot.getString("pass");
            }
        });

        // On clicking the register button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String pass = passwordinp.getText().toString().trim();
                String fnmame = name.getText().toString();
                String phone = phonenum.getText().toString();

                ///if any field is empty
                if((TextUtils.isEmpty(fnmame))){
                    name.setError("password is required");
                    return;
                }
                if (TextUtils.isEmpty(mail) || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    email.setError("Email is required");
                    return;
                }
                if((TextUtils.isEmpty(phone))){
                    phonenum.setError("password is required");
                    return;
                }
                boolean x = isValid(pass);
                if(x ==false){
                    passwordinp.setError("password should have 8 charachters incl 1 capital letter and 1 special character");
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                //Regester user in firebase
                fAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            userID1 = fAuth.getCurrentUser().getUid();

                            DocumentReference documentrefrence1 = fstore.collection("Employer").document(userID).collection("Employees").document(userID1);
                            Map<String,Object> userinfo = new HashMap<>();
                            userinfo.put("fname", fnmame);
                            userinfo.put("email",mail);
                            userinfo.put("phone",phone);
                            userinfo.put("userID",userID1);
                            documentrefrence1.set(userinfo);

                            DocumentReference documentrefrence2 = fstore.collection("users").document(userID1);
                            Map<String,Object> userinfo2 = new HashMap<>();
                            userinfo2.put("fname", fnmame);
                            userinfo2.put("email",mail);
                            userinfo2.put("phone",phone);
                            userinfo2.put("isUser","1");
                            userinfo2.put("password",pass);
                            userinfo2.put("userID",userID1);
                            userinfo2.put("Employer_UserID",userID);

                            documentrefrence2.set(userinfo2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    senEmail(fnmame,mail,pass);

                                }
                            });
                            fAuth.signInWithEmailAndPassword(currentemail, currentpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess (AuthResult authResult){
                                    Toast.makeText(Employer_addemployee.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Employer_home.class));
                                }
                            });
                        }
                        else {
                            Toast.makeText(Employer_addemployee.this, "Error ! \n"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
    }

    private boolean isValid(String password) {
        if (!((password.length() >= 8)
                && (password.length() <= 15))) {
            return false;
        }

        // to check space
        if (password.contains(" ")) {
            return false;
        }
        if (true) {
            int count = 0;

            // check digits from 0 to 9
            for (int i = 0; i <= 9; i++) {

                // to convert int to string
                String str1 = Integer.toString(i);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }
        // for special characters
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {
            return false;
        }

        if (true) {
            int count = 0;

            // checking capital letters
            for (int i = 65; i <= 90; i++) {

                // type casting
                char c = (char)i;

                String str1 = Character.toString(c);
                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        if (true) {
            int count = 0;

            // checking small letters
            for (int i = 90; i <= 122; i++) {

                // type casting
                char c = (char)i;
                String str1 = Character.toString(c);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }
        // if all conditions fails
        return true;
    }

    public void employeraddemployee_to_employer_home(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_home.class);
        startActivity(intent);
        finish();
    }
    public void employeraddemployee_to_employer_calender(View v) {
        //Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_calender.class);
        startActivity(intent);
        finish();
    }
    public void employeraddemployee_to_employer_profile(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Employer_profile.class);
        startActivity(intent);
        finish();
    }

    private void senEmail(String fnmame, String mail, String pass) {
        String mEmail = mail;
        String mSubject = "Successfully Registered";
        String mMessage = "Dear " + fnmame + ", " + "\n" +"Welcome to IBM Attendance App"+"\n"+ "Your account has been created with credentials: " + "\n"+"\n"+"\n"+"\n" +"Email: "+ mEmail + "\n" +"password: "+ pass;
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();

    }
}