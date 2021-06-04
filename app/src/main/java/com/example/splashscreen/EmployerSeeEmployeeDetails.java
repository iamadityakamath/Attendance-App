package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class EmployerSeeEmployeeDetails extends AppCompatActivity {
    ImageView employer_see_employee_img;
    TextView detailname,detailemail,detailphone;
    TextView employer_see_emplyee_yearly_present,employer_see_emplyee_Yearly_total,employer_see_emplyee_Yearly_leave;
    TextView employer_see_emplyee_monthly_total,employer_see_emplyee_monthly_leaves,employer_see_emplyee_monthly_present;
    Spinner employer_see_emplyee_month_spinner,employer_see_emplyee_year_spinner;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;
    String userID,fname,email,phone;
    FirebaseUser user;
    ImageView detailprofileimage;
    ExampleItem Items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employer_see_employee_details);

        detailname = findViewById(R.id.employeeName);
        detailemail = findViewById(R.id.employeeEmail);
        detailphone = findViewById(R.id.employeeContact);
        employer_see_employee_img = findViewById(R.id.employer_see_employee_img);

        employer_see_emplyee_yearly_present = findViewById(R.id.employer_see_emplyee_yearly_present);
        employer_see_emplyee_Yearly_total = findViewById(R.id.employer_see_emplyee_Yearly_total);
        employer_see_emplyee_Yearly_leave = findViewById(R.id.employer_see_emplyee_Yearly_leave);

        employer_see_emplyee_monthly_total = findViewById(R.id.employer_see_emplyee_monthly_total);
        employer_see_emplyee_monthly_leaves = findViewById(R.id.employer_see_emplyee_monthly_leaves);
        employer_see_emplyee_monthly_present = findViewById(R.id.employer_see_emplyee_monthly_present);

        employer_see_emplyee_month_spinner = findViewById(R.id.employer_see_emplyee_month_spinner);
        employer_see_emplyee_year_spinner = findViewById(R.id.employer_see_emplyee_year_spinner);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storagerefrence = FirebaseStorage.getInstance().getReference();


        final Object obj = getIntent().getSerializableExtra("details");
        if(obj instanceof ExampleItem){
            Items = (ExampleItem) obj;
        }

        if(Items != null){
            detailname.setText(Items.getFname());
            detailemail.setText(String.valueOf(Items.getEmail()));
            detailphone.setText(Items.getPhone());
            Log.d("current_user_pic", ""+Items.getUserID());
        }

        SetProfilePic();
    }

    private void SetProfilePic() {
        StorageReference profileRef;
        if ( storagerefrence.child("users/"+Items.getUserID()+"/Profile.jpg")==null){
            return;
        }
//        profileRef = storagerefrence.child("users/"+Items.getUserID()+"/Profile.jpg").;
        storagerefrence.child("users/"+Items.getUserID()+"/Profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(employer_see_employee_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("image_doesnot_exsist", ""+e);
            }
        });
    }

    public void employer_employeedetails_to_home(View v) {
        Intent intent = new Intent(this, Employer_home.class);
        startActivity(intent);
    }
    public void employer_employeedetails_to_calendar(View v) {
        Intent intent = new Intent(this, Employer_calender.class);
        startActivity(intent);
    }

    public void employer_employeedetails_to_addemployee(View view){
        startActivity(new Intent(getApplicationContext(),Employer_addemployee.class));
        finish();
    }
    public void employer_employeedetails_to_profile(View view){
        startActivity(new Intent(getApplicationContext(),Employer_profile.class));
        finish();
    }
}