package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.O)
public class EmployerSeeEmployeeDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView employer_see_employee_img;
    TextView detailname,detailemail,detailphone;
    TextView employer_see_emplyee_yearly_present,employer_see_emplyee_Yearly_total,employer_see_emplyee_Yearly_leave;
    TextView employer_see_emplyee_monthly_total,employer_see_emplyee_monthly_leaves,employer_see_emplyee_monthly_present;
    Spinner employer_see_emplyee_month_spinner,employer_see_emplyee_year_spinner;
    int total_full_days = 0,total_month_full_days=0,total_month_presentdays=0;
    String userID;
    String selectedMonthPos;
    String selectedYear;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;
    ExampleItem Items = null;

    public ArrayList<String> list_dates = new ArrayList<>();
    public ArrayList<Integer> list_hours = new ArrayList<>();
    public ArrayList<String> list_month = new ArrayList<String>();
    public ArrayList<String> list_year = new ArrayList<String>();

    LocalDate currentdate = LocalDate.now();
    int currentmonth = currentdate.getMonthValue()-1;
    DecimalFormat df = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_see_employee_details);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.employer_Dashboad:
                        startActivity(new Intent(getApplicationContext(),Employer_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_add_user:
                        startActivity(new Intent(getApplicationContext(),Employer_addemployee.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_calendar:
                        startActivity(new Intent(getApplicationContext(),Employer_calender.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_Profile:
                        startActivity(new Intent(getApplicationContext(),Employer_profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employer_home:
                        startActivity(new Intent(getApplicationContext(),Employer_search.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });

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
        }
        userID = Items.getUserID();
        SetProfilePic();
        SetyearlyAttendance();
        PopulateSpinnerMonth();
        PopulateSpinnerYear();
        employer_see_emplyee_month_spinner.setOnItemSelectedListener(this);
        employer_see_emplyee_year_spinner.setOnItemSelectedListener(this);
    }

    private void SetProfilePic() {
        StorageReference profileRef;
        if ( storagerefrence.child("users/"+Items.getUserID()+"/Profile.jpg")==null){
            return;
        }
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

    private void SetyearlyAttendance() {
        CollectionReference db1 = fstore.collection("users").document(userID).collection("Daily");
        db1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> clist = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : clist) {
                        getattendancedata p = d.toObject(getattendancedata.class);
                        list_hours.add(p.getHours());
                        list_dates.add(p.getCheckin_date_());
                    }
                    ///Yearly calculation
                    for(int i = 0; i < list_hours.size(); i++) {
                        if (list_hours.get(i) > 200) {
                            total_full_days = total_full_days + 1;
                        }
                    }
                    for(int i = 0; i < list_dates.size(); i++) {
                        String [] dateParts = list_dates.get(i).split("-");
                        String month = dateParts[1];
                        String year = dateParts[2];
                        list_month.add(month);
                        list_year.add(year);
                    }

                    String yearly_presentdays = Integer.toString(total_full_days);
                    employer_see_emplyee_yearly_present.setText(yearly_presentdays);

                    String yearly_totaldays = Integer.toString(list_hours.size());
                    employer_see_emplyee_Yearly_total.setText(yearly_totaldays);

                    String yearly_absent = Integer.toString(list_hours.size()-total_full_days);
                    employer_see_emplyee_Yearly_leave.setText(yearly_absent);
                }

            }
        });
    }
    private void PopulateSpinnerYear() {
        ArrayAdapter<String> yearadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.year_array));
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employer_see_emplyee_year_spinner.setAdapter(yearadapter);
    }
    private void PopulateSpinnerMonth() {
        ArrayAdapter<CharSequence>  monthadapter = ArrayAdapter.createFromResource(this, R.array.months_array, android.R.layout.simple_spinner_item);
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employer_see_emplyee_month_spinner.setAdapter(monthadapter);
        employer_see_emplyee_month_spinner.setSelection(currentmonth);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.employer_see_emplyee_year_spinner){
            int year_pose = parent.getSelectedItemPosition();
            String[] x = {"2021" ,"2022", "2023", "2024","2025","2026" } ;
            selectedYear = x[year_pose];
        }
        if (parent.getId()==R.id.employer_see_emplyee_month_spinner) {
            int c = (int) parent.getSelectedItemPosition()+1;
            selectedMonthPos  = df.format(c); // 0009
            total_month_presentdays=0;
            total_month_full_days=0;

            CollectionReference db1 = fstore.collection("users").document(userID).collection("Daily");
            db1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (int i = 0; i < list_dates.size(); i++) {
                        if (Objects.equals(list_month.get(i), selectedMonthPos) && Objects.equals(list_year.get(i), selectedYear)) {
                            total_month_full_days = total_month_full_days + 1;
                            if (list_hours.get(i) > 0) {
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

                    String monthly_absent = Integer.toString(total_month_full_days-total_month_presentdays);
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
        }
        else if (parent.getId() == R.id.employer_see_emplyee_month_spinner) {
            int c = (int) parent.getSelectedItemPosition() + 1;
            selectedMonthPos = df.format(c); // 0009
        }
    }

    public void employer_employeedetails_to_download_employee_details(View v) {
        Intent intent = new Intent(this, employee_seeAttendance.class);
        intent.putExtra("userID", Items.getUserID());
        startActivity(intent);
    }
}