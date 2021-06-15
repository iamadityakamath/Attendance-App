package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class EmployeeViewAttendance extends AppCompatActivity {

    private ViewAttendanceAdapter myAdapter;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    private String UserID = fAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookref = db.collection("users").document(UserID).collection("Daily");

    private FAdapViewAttendance adapter;

    StringBuilder ViewAttendance = new StringBuilder();
    public ArrayList<String> ViewDataDate = new ArrayList<>();
    public ArrayList<String> ViewDataCheckin = new ArrayList<>();
    public ArrayList<String> ViewDataCheckout = new ArrayList<>();
    public ArrayList<String> ViewDataMinutes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_employee_view_attendance);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.employee_home:
                        startActivity(new Intent(getApplicationContext(),employee_home.class));
                        overridePendingTransition(0,0);
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

        SetUpRecyclerView();


    }

    private void SetUpRecyclerView() {
        Query query = notebookref.orderBy(String.valueOf("checkin_date_"),Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ViewAttendanceAdapter> options = new FirestoreRecyclerOptions
                .Builder<ViewAttendanceAdapter>()
                .setQuery(query, ViewAttendanceAdapter.class)
                .build();

        adapter = new FAdapViewAttendance(options);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAtteandance);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}