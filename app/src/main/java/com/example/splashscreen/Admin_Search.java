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
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Admin_Search extends AppCompatActivity {
    public RecyclerView aRecyclerView;
    public Admin_Employer_ADAPTER aAdapter;
    public ArrayList<Admin_Employer> adminemployerlist;
    public String EmployerID;
    EditText searchh;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    CharSequence searchC = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__search);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation3);
        bottomNavigationView.setSelectedItemId(R.id.admin_Profile_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.admin_Dashboad_nav:
                        startActivity(new Intent(getApplicationContext(), Admin_home.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.admin_add_user_nav:
                        startActivity(new Intent(getApplicationContext(), Admin_addEmployer.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.admin_search_nav:
                        return true;

                    case R.id.admin_calendar_nav:
                        startActivity(new Intent(getApplicationContext(), Admin_Calendar.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.admin_Profile_nav:
                        startActivity(new Intent(getApplicationContext(), Admin_Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        db = FirebaseFirestore.getInstance();

        adminemployerlist = new ArrayList<>();

        fAuth = FirebaseAuth.getInstance();
        //EmployerID = fAuth.getCurrentUser().getUid();

        aRecyclerView = (RecyclerView) findViewById(R.id.Admin_employer_recycler_view);
        aRecyclerView.setHasFixedSize(true);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchh = findViewById(R.id.edittextEmployer);

        adminemployerlist = new ArrayList<>();
        aAdapter = new Admin_Employer_ADAPTER(adminemployerlist);

        aRecyclerView.setAdapter(aAdapter);

        db.collection("Employer").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot a : list) {

                                Admin_Employer p = a.toObject(Admin_Employer.class);
                                //p.setId(a.getId());
                                adminemployerlist.add(p);

                            }
                            aAdapter.notifyDataSetChanged();
                        }
                    }
                });



        //search
        searchh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aAdapter.getFilter().filter(s);
                searchC = s;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
