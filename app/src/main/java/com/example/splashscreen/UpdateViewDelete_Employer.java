package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthResult;
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

import org.jetbrains.annotations.NotNull;

import static com.google.firebase.firestore.FieldValue.delete;

public class UpdateViewDelete_Employer extends AppCompatActivity implements View.OnClickListener{

    private TextView editTextName;
    private TextView editTextEmail;
    private TextView editTextPass;
    private TextView editTextPhone;
    private TextView editTextUserID;
    FirebaseAuth fAuth;
    private FirebaseFirestore db;
    StorageReference storagerefrence;
    Admin_Employer Items = null;
    String userID;
    String Admin_email,Admin_pass,Admin_userid;

    private Admin_Employer adminEmployer;
    FirebaseUser Employer;

    ImageView admin_see_employer_image;
    public CardView aeupdate,aedelete,button;

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

        DocumentReference documentReference = db.collection("Admin").document("FGWUYBcerxMb456ecwuIxvbQJ8L2");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Admin_email = documentSnapshot.getString("email");
                Admin_pass = documentSnapshot.getString("admin_pass");
                Log.d("Delete_from_data", " "+ Admin_email);
                Log.d("Delete_from_data", " "+ Admin_pass);
            }
        });

        //userID = fAuth.getCurrentUser().getUid();
        Log.d("hbdsfv", ""+adminEmployer.getUserID());
        Log.d("hbdsfvjkjj", ""+userID);
        Log.d("hbdree",""+ adminEmployer.getFname());
        SetProfilePic();
        //Employer = fAuth.getCurrentUser();

        editTextName = findViewById(R.id.Employer_Detail_name);
        editTextEmail = findViewById(R.id.Employer_Detail_Email);
        editTextPhone = findViewById(R.id.Employer_Detail_Phone);
        //editTextUserID = findViewById(R.id.Employer_Detail_userid);

        button = findViewById(R.id.Employer_Detail_Employee_Button2);


        editTextName.setText(adminEmployer.getFname());
        editTextEmail.setText(adminEmployer.getEmail());
        editTextPhone.setText(String.valueOf(adminEmployer.getPhone()));
        //editTextUserID.setText(adminEmployer.getUserID());


        admin_see_employer_image = findViewById(R.id.admin_see_employer_image);

        //aeupdate = (CardView) findViewById(R.id.Employer_Detail_Update_Button);
        aedelete = (CardView) findViewById(R.id.Employer_Detail_Delete_Button);
        button = (CardView) findViewById(R.id.Employer_Detail_Employee_Button2);

        //aeupdate.setOnClickListener(this);
        aedelete.setOnClickListener(this);
        button.setOnClickListener(this);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation3);
        bottomNavigationView.setSelectedItemId(R.id.admin_calendar_nav);

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
                        startActivity(new Intent(getApplicationContext(),Admin_addEmployer.class));
                        overridePendingTransition(0,0);
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

    }

    private void SetProfilePic() {
        StorageReference profileRef;
        if (storagerefrence.child("users/" + adminEmployer.getUserID() + "/Profile.jpg") == null) {

            return;
        }
        storagerefrence.child("users/" + adminEmployer.getUserID() + "/Profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(admin_see_employer_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("pic_doesnot_exist", "" + e);
            }
        });
    }

    private boolean hasValidationErrors(String name, String email, String phone, String userID) {
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

        /*if (pass.isEmpty()) {
            editTextPass.setError("Pass required");
            editTextPass.requestFocus();
            return true;
        }*/

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


    /*private void updateAdmin_Employer() {
        String fname = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        //String pass = editTextPass.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String userID = editTextUserID.getText().toString().trim();

        if (!hasValidationErrors(fname, email, phone, userID)) {

            Admin_Employer p = new Admin_Employer (
                    fname,email,phone,userID
            );

            //asedb.collection("Employer").document(items.getUserID()).collection("Employees").get()
            db.collection("Employer").document(adminEmployer.getUserID())
                    .update(

                            "fname", adminEmployer.getFname(),
                            "email", adminEmployer.getEmail(),
                            //"pass", p.getPass(),
                            "phone", adminEmployer.getPhone(),
                            "UserID", adminEmployer.getUserID()

                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdateViewDelete_Employer.this, "Employer Account Updated", Toast.LENGTH_LONG).show();

                        }

                    });
            Intent intent= new Intent(this, Admin_home.class);
            startActivity(intent);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.Employer_Detail_Update_Button:
            //   updateAdmin_Employer();
            //   break;

            case R.id.Employer_Detail_Delete_Button:
                AlertDialog.Builder deleteuser = new AlertDialog.Builder(v.getContext());
                deleteuser.setTitle("Are you sure you want to delete this account?");
                deleteuser.setMessage("Deletion is permanent");

                deleteuser.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAdmin_Employer();
                    }
                });

                deleteuser.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                deleteuser.show();
                break;
            case R.id.Employer_Detail_Employee_Button2:
                Intent intent = new Intent(UpdateViewDelete_Employer.this, AdminSeeEmployee.class);
                intent.putExtra("employeruserID", Items.getUserID());
                startActivity(intent);
                break;
        }
    }



    private void deleteAdmin_Employer() {
        String x = Items.getUserID();
        DocumentReference documentrefrence2 = db.collection("Employer").document(x);
        documentrefrence2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("isEmployer") != null) {
                    String ee_email = documentSnapshot.getString("email");
                    String ee_pass = documentSnapshot.getString("pass");
                    fAuth.signInWithEmailAndPassword(ee_email, ee_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser usera = FirebaseAuth.getInstance().getCurrentUser();
                            usera.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        loginagain();
                                    }
                                }

                            });
                        }
                    });
                }
            }
        });
    }

    public void loginagain(){
        fAuth.signInWithEmailAndPassword(Admin_email, Admin_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess (AuthResult authResult){
                Admin_userid = fAuth.getCurrentUser().getUid();
                Log.d("Delete_from_data", "logged in using ");
                Toast.makeText(UpdateViewDelete_Employer.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateViewDelete_Employer.this, Admin_home.class));
            }
        });
    }

}