package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class Admin_addEmployer extends AppCompatActivity {
    Button admin_add_regesterbutton;
    private EditText name, email, phonenum, passwordinp;
    String currentemail,currentpass;
    ProgressBar progressbar;
    private FirebaseAuth fAuth1;
    FirebaseFirestore fstore;
    String userID;
    String userID1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_employer);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation3);
        bottomNavigationView.setSelectedItemId(R.id.admin_add_user_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.admin_Dashboad_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.admin_add_user_nav:
                        return true;

                    case R.id.admin_search_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_Search.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.admin_calendar_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_Calendar.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.admin_Profile_nav:
                        startActivity(new Intent(getApplicationContext(),Admin_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        admin_add_regesterbutton = findViewById(R.id.admin_add_regesterbutton);
        name = findViewById(R.id.admin_add_name);
        email = findViewById(R.id.admin_add_email);
        phonenum = findViewById(R.id.admin_add_phone);
        passwordinp = findViewById(R.id.admin_add_password);
        progressbar = findViewById(R.id.Regester_progressBar);

        fAuth1 = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth1.getCurrentUser().getUid();

        DocumentReference df = fstore.collection("Admin").document(userID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentemail = documentSnapshot.getString("email");
                currentpass = documentSnapshot.getString("admin_pass");
                Log.d("current", ""+currentemail);
                Log.d("current", ""+currentpass);
            }
        });

        admin_add_regesterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newmail = email.getText().toString().trim();
                String pass = passwordinp.getText().toString().trim();
                String fnmame = name.getText().toString();
                String phone = phonenum.getText().toString();

                ///if any field is empty
                if((TextUtils.isEmpty(fnmame))){
                    name.setError("password is required");
                    return;
                }

                if (TextUtils.isEmpty(newmail) || !Patterns.EMAIL_ADDRESS.matcher(newmail).matches()) {
                    email.setError("Email is required");
                    return;
                }

                if((TextUtils.isEmpty(phone)) || !phone.matches("\\d{10}")){
                    phonenum.setError("Phone no. is incorrect");
                    return;
                }
                boolean x = isValid(pass);
                Log.d("entering_this_loop", " "+pass);
                if(x ==false){

                    passwordinp.setError("password should have 8 charachters incl 1 capital letter and 1 special character");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);
                //Regester user in firebase
                fAuth1.createUserWithEmailAndPassword(newmail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            userID1 = fAuth1.getCurrentUser().getUid();
                            DocumentReference documentrefrence3 = fstore.collection("Employer").document(userID1);
                            Map<String,Object> userinfo = new HashMap<>();
                            userinfo.put("fname", fnmame);
                            userinfo.put("email",newmail);
                            userinfo.put("phone",phone);
                            userinfo.put("isEmployer","1");
                            userinfo.put("pass", pass);
                            userinfo.put("UserID",userID1);
                            documentrefrence3.set(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Admin_addEmployer.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    senEmail(fnmame,newmail,pass);
                                }
                            });

                            fAuth1.signInWithEmailAndPassword(currentemail, currentpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess (AuthResult authResult){
                                        startActivity(new Intent(getApplicationContext(), Admin_home.class));
                                    }
                            });
                        }
                        else {
                            progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Admin_addEmployer.this, "Error ! \n"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }


    // A utility function to check
        // whether a password is valid or not
        public static boolean isValid(String password)
        {Log.d("entering_this_loop", " "+password);

            // for checking if password length
            // is between 8 and 15
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


    private void senEmail(String fnmame, String mail, String pass) {
        String mEmail = mail;
        String mSubject = "Successfully Registered";
        String mMessage = "Dear " + fnmame + ", " + "\n" +"Welcome to IBM Attendance App"+"\n"+ "Your account has been created with credentials: " + "\n"+"\n" +"Email: "+ mEmail + "\n" +"password: "+ pass+"\n"+"\n"+"\n"+"\n" +"To reset the password, go to profile-> Edit Profile -> Reset Password"+"\n"+"\n"+"\n"+"\n"+"Click on the below link to download the app"+"\n\n\n\n"+"https://drive.google.com/drive/folders/1tXStdKMSAqqAHrppR8YEVZBQpZiDtd3y?usp=sharing";;
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();
    }
}