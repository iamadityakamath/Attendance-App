package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.O)
public class EmployerSeeEmployeeDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView employer_see_employee_img,deleteemploye;
    TextView detailname, detailemail, detailphone,dateofbirth;
    TextView employer_see_emplyee_yearly_present, employer_see_emplyee_Yearly_total, employer_see_emplyee_Yearly_leave;
    TextView employer_see_emplyee_monthly_total, employer_see_emplyee_monthly_leaves, employer_see_emplyee_monthly_present;
    Spinner employer_see_emplyee_month_spinner, employer_see_emplyee_year_spinner,employer_see_emplyee_total_year_spinner;
    int total_full_days = 0, total_month_full_days = 0, total_month_presentdays = 0;
    String userID,Employer_userid;
    String Employer_email,Employer_pass,selectedMonthPos;
    String date_of_join;
    String selectedYear,selectedYeartotal;
    Double adminmaxminuts;
    int totaldays;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;
    ExampleItem Items = null;

    public ArrayList<String> list_dates = new ArrayList<>();
    public ArrayList<Integer> list_hours = new ArrayList<>();
    public ArrayList<String> list_month = new ArrayList<String>();
    public ArrayList<String> list_year = new ArrayList<String>();

    LocalDate currentdate = LocalDate.now();
    int currentmonth = currentdate.getMonthValue() - 1;
    DecimalFormat df = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_see_employee_details);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.employer_Dashboad:
                        startActivity(new Intent(getApplicationContext(), Employer_home.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.employer_add_user:
                        startActivity(new Intent(getApplicationContext(), Employer_addemployee.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.employer_calendar:
                        startActivity(new Intent(getApplicationContext(), Employer_calender.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.employer_Profile:
                        startActivity(new Intent(getApplicationContext(), Employer_profile.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.employer_home:
                        startActivity(new Intent(getApplicationContext(), Employer_search.class));
                        overridePendingTransition(0, 0);
                }
                return false;
            }
        });

        detailname = findViewById(R.id.employeeName);
        detailemail = findViewById(R.id.employeeEmail);
        detailphone = findViewById(R.id.employeeContact);
        dateofbirth = findViewById(R.id.dateofbirth);
        deleteemploye = findViewById(R.id.deleteemploye);
        employer_see_employee_img = findViewById(R.id.employer_see_employee_img);

        employer_see_emplyee_yearly_present = findViewById(R.id.employer_see_emplyee_yearly_present);
        employer_see_emplyee_Yearly_total = findViewById(R.id.employer_see_emplyee_Yearly_total);
        employer_see_emplyee_Yearly_leave = findViewById(R.id.employer_see_emplyee_Yearly_leave);

        employer_see_emplyee_monthly_total = findViewById(R.id.employer_see_emplyee_monthly_total);
        employer_see_emplyee_monthly_leaves = findViewById(R.id.employer_see_emplyee_monthly_leaves);
        employer_see_emplyee_monthly_present = findViewById(R.id.employer_see_emplyee_monthly_present);

        employer_see_emplyee_month_spinner = findViewById(R.id.employer_see_emplyee_month_spinner);
        employer_see_emplyee_year_spinner = findViewById(R.id.employer_see_emplyee_year_spinner);
        employer_see_emplyee_total_year_spinner = findViewById(R.id.employer_see_emplyee_total_year_spinner);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storagerefrence = FirebaseStorage.getInstance().getReference();
        Employer_userid = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("Employer").document(Employer_userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Employer_email = documentSnapshot.getString("email");
                Employer_pass = documentSnapshot.getString("pass");
                Log.d("Delete_from_data", " "+Employer_email);
                Log.d("Delete_from_data", " "+Employer_pass);
            }
        });

        DocumentReference db2 = fstore.collection("Admin").document("FGWUYBcerxMb456ecwuIxvbQJ8L2");
        db2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                adminmaxminuts = documentSnapshot.getDouble("maxMinutes");
                SetProfilePic();
                SetyearlyAttendance();
                PopulateSpinnerMonth();
                PopulateSpinnerYear();
                PopulateSpinnerYeartotal();
            }
        });



        final Object obj = getIntent().getSerializableExtra("details");
        if (obj instanceof ExampleItem) {
            Items = (ExampleItem) obj;
        }
        if (Items != null) {
            detailname.setText(Items.getFname());
            detailemail.setText(String.valueOf(Items.getEmail()));
            detailphone.setText(Items.getPhone());
        }
        userID = Items.getUserID();

        DocumentReference emp = fstore.collection("users").document(userID);
        emp.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                date_of_join = documentSnapshot.getString("Date of joining");
                Log.d("date_of", ""+date_of_join);
                dateofbirth.setText("Date of joining: " + date_of_join);
            }
        });



        employer_see_emplyee_month_spinner.setOnItemSelectedListener(this);
        employer_see_emplyee_year_spinner.setOnItemSelectedListener(this);
        employer_see_emplyee_total_year_spinner.setOnItemSelectedListener(this);


        deleteemploye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteuser =new AlertDialog.Builder(v.getContext());
                deleteuser.setTitle("Delete User");
                deleteuser.setMessage("Are you sure you want to delete the user");
                deleteuser.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String x = Items.getUserID();
                        DocumentReference documentrefrence2 = fstore.collection("users").document(x);
                        documentrefrence2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.getString("isUser") != null) {
                                    String e_email = documentSnapshot.getString("email");
                                    String e_pass = documentSnapshot.getString("password");
                                    fAuth.signInWithEmailAndPassword(e_email, e_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            FirebaseUser usere = FirebaseAuth.getInstance().getCurrentUser();
                                            usere.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                });

                deleteuser.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialouge

                    }
                });

                deleteuser.show();
            }
        });
    }

    private void SetProfilePic() {
        StorageReference profileRef;
        if (storagerefrence.child("users/" + Items.getUserID() + "/Profile.jpg") == null) {
            return;
        }
        storagerefrence.child("users/" + Items.getUserID() + "/Profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(employer_see_employee_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("image_doesnot_exsist", "" + e);
            }
        });
    }

    private void SetyearlyAttendance() {
        CollectionReference db1 = fstore.collection("users").document(userID).collection("Daily");
        db1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> clist = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : clist) {
                        getattendancedata p = d.toObject(getattendancedata.class);
                        list_hours.add(p.getMinutes());
                        list_dates.add(p.getCheckin_date_());
                    }

                    for (int i = 0; i < list_dates.size(); i++) {
                        String[] dateParts = list_dates.get(i).split("-");
                        String month = dateParts[1];
                        String year = dateParts[2];
                        list_month.add(month);
                        list_year.add(year);
                    }
                    ///Yearly calculation
                    for (int i = 0; i < list_dates.size(); i++) {
                        if ( Objects.equals(list_year.get(i), selectedYeartotal)) {
                            if (list_hours.get(i) > adminmaxminuts) {
                                total_full_days = total_full_days + 1;
                            }
                            totaldays = totaldays+1;
                        }
                    }

                    String yearly_presentdays = Integer.toString(total_full_days);
                    employer_see_emplyee_yearly_present.setText(yearly_presentdays);

                    String yearly_totaldays = Integer.toString(list_hours.size());
                    employer_see_emplyee_Yearly_total.setText(yearly_totaldays);

                    String yearly_absent = Integer.toString(list_hours.size() - total_full_days);
                    employer_see_emplyee_Yearly_leave.setText(yearly_absent);
                }

            }
        });
    }

    private void onselectingyear(){
        total_full_days = 0;
        totaldays = 0;
        for (int i = 0; i < list_dates.size(); i++) {
            if ( Objects.equals(list_year.get(i), selectedYeartotal)) {
                if (list_hours.get(i) > adminmaxminuts) {
                    total_full_days = total_full_days + 1;
                }
                totaldays = totaldays+1;
            }
        }
        //Log.d("value_of_i", ""+i);
        String yearly_presentdays = Integer.toString(total_full_days);
        employer_see_emplyee_yearly_present.setText(yearly_presentdays);

        String yearly_totaldays = Integer.toString(totaldays);
        employer_see_emplyee_Yearly_total.setText(yearly_totaldays);

        String yearly_absent = Integer.toString(totaldays - total_full_days);
        employer_see_emplyee_Yearly_leave.setText(yearly_absent);
    }
    private void PopulateSpinnerYear() {
        ArrayAdapter<String> yearadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.year_array));
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employer_see_emplyee_year_spinner.setAdapter(yearadapter);
    }
    private void PopulateSpinnerMonth() {
        ArrayAdapter<CharSequence> monthadapter = ArrayAdapter.createFromResource(this, R.array.months_array, android.R.layout.simple_spinner_item);
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employer_see_emplyee_month_spinner.setAdapter(monthadapter);
        employer_see_emplyee_month_spinner.setSelection(currentmonth);
    }
    private void PopulateSpinnerYeartotal() {
        ArrayAdapter<String> yearadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.year_array));
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employer_see_emplyee_total_year_spinner.setAdapter(yearadapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.employer_see_emplyee_total_year_spinner){
            int total_year_pos = parent.getSelectedItemPosition();
            String[] x = {"2021" ,"2022", "2023", "2024","2025","2026" } ;
            selectedYeartotal = x[total_year_pos];
            onselectingyear();
        }

        if (parent.getId() == R.id.employer_see_emplyee_year_spinner) {
            int year_pose = parent.getSelectedItemPosition();
            String[] x = {"2021", "2022", "2023", "2024", "2025", "2026"};
            selectedYear = x[year_pose];
            if (selectedYear != "2021") {
                employer_see_emplyee_month_spinner.setSelection(0);
                selectedMonthPos = df.format(0);
            }
            if (selectedYear == "2021") {
                employer_see_emplyee_month_spinner.setSelection(currentmonth);
                selectedMonthPos = df.format(currentmonth + 1);
            }

        }
        if (parent.getId() == R.id.employer_see_emplyee_month_spinner) {
            int c = (int) parent.getSelectedItemPosition() + 1;
            selectedMonthPos = df.format(c); // 0009
            total_month_presentdays = 0;
            total_month_full_days = 0;

            CollectionReference db1 = fstore.collection("users").document(userID).collection("Daily");
            db1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (int i = 0; i < list_dates.size(); i++) {
                        if (Objects.equals(list_month.get(i), selectedMonthPos) && Objects.equals(list_year.get(i), selectedYear)) {
                            total_month_full_days = total_month_full_days + 1;
                            if (list_hours.get(i) > adminmaxminuts) {
                                total_month_presentdays = total_month_presentdays + 1;
                            }
                        } else {
                            employer_see_emplyee_monthly_present.setText("0");
                            employer_see_emplyee_monthly_total.setText("0");
                            employer_see_emplyee_monthly_leaves.setText("0");
                        }
                    }

                    String monthly_presentdays = Integer.toString(total_month_presentdays);
                    employer_see_emplyee_monthly_present.setText(monthly_presentdays);

                    String monthly_totaldays = Integer.toString(total_month_full_days);
                    employer_see_emplyee_monthly_total.setText(monthly_totaldays);

                    String monthly_absent = Integer.toString(total_month_full_days - total_month_presentdays);
                    employer_see_emplyee_monthly_leaves.setText(monthly_absent);

                }
            });

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.employer_see_emplyee_year_spinner) {
            int year_pose = parent.getSelectedItemPosition();
            String[] x = {"2021", "2022", "2023", "2024", "2025", "2026"};
            selectedYear = x[year_pose];
        } else if (parent.getId() == R.id.employer_see_emplyee_month_spinner) {
            int c = (int) parent.getSelectedItemPosition() + 1;
            selectedMonthPos = df.format(c); // 0009
        }
    }

    public void employer_employeedetails_to_download_employee_details(View v) {
        Intent intent = new Intent(this, employee_seeAttendance.class);
        intent.putExtra("userID", Items.getUserID());
        startActivity(intent);
    }


    public void loginagain(){
        fAuth.signInWithEmailAndPassword(Employer_email, Employer_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess (AuthResult authResult){
                Employer_userid = fAuth.getCurrentUser().getUid();
                Log.d("Delete_from_data", "logged in using ");
                Toast.makeText(EmployerSeeEmployeeDetails.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Employer_home.class));
            }
        });
    }
}