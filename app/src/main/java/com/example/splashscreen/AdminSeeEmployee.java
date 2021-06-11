package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminSeeEmployee extends AppCompatActivity {

    private RecyclerView aseRecyclerView;
    private AdminEmployeeAdapter aseAdapter;
    private ArrayList<AdminEmployee> adminEmployeeList;
    private String EmployeeID;
    public AdminEmployee items = null;

    EditText asesearchh;
    CharSequence asesearchC = "";

    FirebaseAuth asefAuth;
    FirebaseFirestore asedb;
    String r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_see_employee);

        asedb = FirebaseFirestore.getInstance();

        adminEmployeeList = new ArrayList<>();

        asefAuth = FirebaseAuth.getInstance();
        EmployeeID = asefAuth.getCurrentUser().getUid();

        aseRecyclerView = (RecyclerView) findViewById(R.id.admin_EMPLOYER_EMPLOYEE_recyclerView);
        aseRecyclerView.setHasFixedSize(true);
        aseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        asesearchh = findViewById(R.id.edittextEmployee);



        adminEmployeeList = new ArrayList<>();
        aseAdapter = new AdminEmployeeAdapter(this, adminEmployeeList);

        aseRecyclerView.setAdapter(aseAdapter);

        Intent intent = getIntent();
        r = intent.getStringExtra("employeruserID");
        Log.d("employeruserID","" + r);
        asedb.collection("Employer").document(r).collection("Employees").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot a : list){

                                AdminEmployee p = a.toObject(AdminEmployee.class);
                                //p.setId(a.getId());
                                adminEmployeeList.add(p);

                            }
                            aseAdapter.notifyDataSetChanged();

                        }
                    }
                });

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

        //search
        asesearchh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aseAdapter.getFilter().filter(s);
                asesearchC = s;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}