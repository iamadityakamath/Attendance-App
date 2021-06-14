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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
public class employee_home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView employee_home_img;
    TextView employee_home_yearly_total,employee_home_yearly_present,employee_home_yearly_leaves;
    TextView employee_home_monthly_total,employee_home_monthly_leave,employee_home_monthly_present;
    int total_full_days = 0,total_month_full_days=0,total_month_presentdays=0;
    int totaldays = 0;
    String userID;
    String selectedMonthPos;
    String selectedYear,selectedYeartotal;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;
    Double adminmaxminuts;

    public ArrayList<String> list_dates = new ArrayList<>();
    public ArrayList<Integer> list_hours = new ArrayList<>();
    public ArrayList<String> list_month = new ArrayList<String>();
    public ArrayList<String> list_year = new ArrayList<String>();


    Spinner employee_home_month_spinner,employee_home_year_spinner,employee_spinner_year_total;
    LocalDate currentdate = LocalDate.now();
    int currentmonth = currentdate.getMonthValue()-1;

    DecimalFormat df = new DecimalFormat("00");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        employee_home_img = findViewById(R.id.employee_home_profilepic);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation2);
        bottomNavigationView.setSelectedItemId(R.id.employee_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.employee_home:
                        return true;

                    case R.id.employee_profile:
                        startActivity(new Intent(getApplicationContext(),employee_profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employee_calendar:
                        startActivity(new Intent(getApplicationContext(),employee_calendar.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        employee_home_yearly_present = findViewById(R.id.employee_home_yearly_present);
        employee_home_yearly_total = findViewById(R.id.employee_home_yearly_total);
        employee_home_yearly_leaves = findViewById(R.id.employee_home_yearly_leaves);

        employee_home_monthly_total = findViewById(R.id.employee_home_monthly_total);
        employee_home_monthly_leave = findViewById(R.id.employee_home_monthly_leave);
        employee_home_monthly_present = findViewById(R.id.employee_home_monthly_present);

        employee_home_month_spinner = findViewById(R.id.employee_home_month_spinner);
        employee_home_year_spinner = findViewById(R.id.employee_home_year_spinner);
        employee_spinner_year_total = findViewById(R.id.employee_spinner_year_total);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storagerefrence = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

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
        employee_home_month_spinner.setOnItemSelectedListener(this);
        employee_home_year_spinner.setOnItemSelectedListener(this);
        employee_spinner_year_total.setOnItemSelectedListener(this);

        
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
                    for(int i = 0; i < list_dates.size(); i++) {
                        String [] dateParts = list_dates.get(i).split("-");
                        String month = dateParts[1];
                        String year = dateParts[2];
                        list_month.add(month);
                        list_year.add(year);
                    }

                    for (int i = 0; i < list_dates.size(); i++) {
                        if ( Objects.equals(list_year.get(i), selectedYeartotal)) {
                            Log.d("List_h", ""+list_hours.get(i));
                            Log.d("List_h_m", ""+adminmaxminuts);
                            if (list_hours.get(i) > adminmaxminuts) {
                                total_full_days = total_full_days + 1;
                            }
                            totaldays = totaldays+1;
                        }
                    }
                    //Log.d("value_of_i", ""+i);
                    String yearly_presentdays = Integer.toString(total_full_days);
                    employee_home_yearly_present.setText(yearly_presentdays);

                    String yearly_totaldays = Integer.toString(totaldays);
                    employee_home_yearly_total.setText(yearly_totaldays);

                    String yearly_absent = Integer.toString(totaldays-total_full_days);
                    employee_home_yearly_leaves.setText(yearly_absent);
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
        employee_home_yearly_present.setText(yearly_presentdays);

        String yearly_totaldays = Integer.toString(totaldays);
        employee_home_yearly_total.setText(yearly_totaldays);

        String yearly_absent = Integer.toString(totaldays-total_full_days);
        employee_home_yearly_leaves.setText(yearly_absent);
    }



    ///Profile  pic

    private void SetProfilePic() {
        StorageReference profileRef = storagerefrence.child("users/"+fAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(employee_home_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("image_doesnot_exsist", ""+e);
            }
        });
    }
    ///Set spinner values
    private void PopulateSpinnerYear() {
        ArrayAdapter<String> yearadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.year_array));
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employee_home_year_spinner.setAdapter(yearadapter);
    }
    private void PopulateSpinnerMonth() {
        ArrayAdapter<CharSequence>  monthadapter = ArrayAdapter.createFromResource(this, R.array.months_array, android.R.layout.simple_spinner_item);
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employee_home_month_spinner.setAdapter(monthadapter);
        employee_home_month_spinner.setSelection(currentmonth);
    }
    private void PopulateSpinnerYeartotal() {
        ArrayAdapter<String> yearadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.year_array));
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employee_spinner_year_total.setAdapter(yearadapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)  {
        if(parent.getId()==R.id.employee_spinner_year_total){
            int total_year_pos = parent.getSelectedItemPosition();
            String[] x = {"2021" ,"2022", "2023", "2024","2025","2026" } ;
            selectedYeartotal = x[total_year_pos];
            onselectingyear();
        }
        if(parent.getId()==R.id.employee_home_year_spinner){
            int year_pose = parent.getSelectedItemPosition();
            String[] x = {"2021" ,"2022", "2023", "2024","2025","2026" } ;
            selectedYear = x[year_pose];
            if(selectedYear!="2021"){
                employee_home_month_spinner.setSelection(0);
                selectedMonthPos = df.format(0);
            }
            if(selectedYear=="2021"){
                employee_home_month_spinner.setSelection(currentmonth);
                selectedMonthPos = df.format(currentmonth+1);
            }
        }
        if (parent.getId()==R.id.employee_home_month_spinner) {
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
                            if (list_hours.get(i) > adminmaxminuts) {
                                total_month_presentdays = total_month_presentdays + 1;
                            }
                        } else {
                            employee_home_monthly_present.setText("0");
                            employee_home_monthly_total.setText("0");
                            employee_home_monthly_leave.setText("0");
                        }
                    }


                    String monthly_presentdays = Integer.toString(total_month_presentdays);
                    employee_home_monthly_present.setText(monthly_presentdays);

                    String monthly_totaldays = Integer.toString(total_month_full_days);
                    employee_home_monthly_total.setText(monthly_totaldays);

                    String monthly_absent = Integer.toString(total_month_full_days-total_month_presentdays);
                    employee_home_monthly_leave.setText(monthly_absent);
                    }
            });

        }


    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.employee_home_year_spinner) {
            int year_pose = parent.getSelectedItemPosition();
            String[] x = {"2021", "2022", "2023", "2024", "2025", "2026"};
            selectedYear = x[year_pose];
        }
        else if (parent.getId() == R.id.employee_home_month_spinner) {
            int c = (int) parent.getSelectedItemPosition() + 1;
            selectedMonthPos = df.format(c); // 0009
        }
    }

    public void employee_Checkinoutpg(View v) {
        Intent intent = new Intent(this, employee_checkinout.class);
        startActivity(intent);
    }


}