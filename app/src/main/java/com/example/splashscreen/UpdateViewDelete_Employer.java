package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import static com.google.firebase.firestore.FieldValue.delete;

public class UpdateViewDelete_Employer extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPass;
    private EditText editTextPhone;
    private EditText editTextUserID;
    FirebaseAuth fAuth;
    private FirebaseFirestore db;
    StorageReference storagerefrence;
    Admin_Employer Items = null;
    String userID;
    public Button button;

    private Admin_Employer adminEmployer;
    FirebaseUser Employer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_view_delete_employer);

        adminEmployer  = (Admin_Employer) getIntent().getSerializableExtra("Admin_Employer_Details");

        if(adminEmployer instanceof Admin_Employer){
            Items = (Admin_Employer) adminEmployer ;
        }

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storagerefrence = FirebaseStorage.getInstance().getReference();

        userID = fAuth.getCurrentUser().getUid();
        Employer = fAuth.getCurrentUser();

        editTextName = findViewById(R.id.Employer_Detail_name);
        editTextEmail = findViewById(R.id.Employer_Detail_Email);
        editTextPass = findViewById(R.id.Employer_Detail_Pass);
        editTextPhone = findViewById(R.id.Employer_Detail_Phone);
        editTextUserID = findViewById(R.id.Employer_Detail_UserID);

        button = findViewById(R.id.Employer_Detail_Employee_Button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateViewDelete_Employer.this, AdminSeeEmployee.class);
                intent.putExtra("employeruserID", Items.getUserID());
                startActivity(intent);
            }

        });

        editTextName.setText(adminEmployer.getFname());
        editTextEmail.setText(adminEmployer.getEmail());
        editTextPass.setText(adminEmployer.getPass());
        editTextPhone.setText(String.valueOf(adminEmployer.getPhone()));
        editTextUserID.setText(String.valueOf(adminEmployer.getUserID()));


        findViewById(R.id.Employer_Detail_Update_Button).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.Employer_Detail_Delete_Button).setOnClickListener((View.OnClickListener) this);
        //findViewById(R.id.Employer_Detail_Employee_Button2).setOnClickListener((View.OnClickListener) this);
    }



    private boolean hasValidationErrors(String name, String email, String pass, String phone, String userID) {
        if (name.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return true;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return true;
        }

        if (pass.isEmpty()) {
            editTextPass.setError("Pass required");
            editTextPass.requestFocus();
            return true;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError("Phone required");
            editTextPhone.requestFocus();
            return true;
        }

        if (userID.isEmpty()) {
            editTextUserID.setError("UserID required");
            editTextUserID.requestFocus();
            return true;
        }
        return false;
    }


    private void updateAdmin_Employer() {
        String fname = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String userID = editTextUserID.getText().toString().trim();

        if (!hasValidationErrors(fname, email, pass, phone, userID)) {

            Admin_Employer p = new Admin_Employer (
                    fname,email,pass,phone,userID
            );

            //asedb.collection("Employer").document(items.getUserID()).collection("Employees").get()
            db.collection("Employer").document(adminEmployer.getUserID())
                    .update(
                            "fname", p.getFname(),
                            "email", p.getEmail(),
                            "pass", p.getPass(),
                            "phone", p.getPhone(),
                            "UserID", p.getUserID()

                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdateViewDelete_Employer.this, "Employer Account Updated", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Employer_Detail_Update_Button:
                updateAdmin_Employer();
                break;

            case R.id.Employer_Detail_Delete_Button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to delete this account?");
                builder.setMessage("Deletion is permanent");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAdmin_Employer();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
                break;
            case R.id.Employer_Detail_Employee_Button2:
                Intent intent = new Intent(this, AdminSeeEmployee.class);
                break;
        }
    }



    private void deleteAdmin_Employer() {
        db.collection("Employer").document(Items.getUserID()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UpdateViewDelete_Employer.this,"Employer Account Deleted", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(UpdateViewDelete_Employer.this, Admin_Search.class));
                        }
                    }
                });
    }



    ///Intent
    public void Employer_Detail_to_admin_add_employer(View v) {
        //Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Admin_addEmployer.class);
        startActivity(intent);
        finish();
    }
    public void Employer_Detail_to_admin_home_button(View v) {
        //Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Admin_home.class);
        startActivity(intent);
        finish();
    }
    public void Employer_Detail_to_admin_profile(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Admin_Profile.class);
        startActivity(intent);
        finish();
    }
    public void Employer_Detail_to_admin_calender(View v) {
        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Admin_Calendar.class);
        startActivity(intent);
        finish();
    }

}