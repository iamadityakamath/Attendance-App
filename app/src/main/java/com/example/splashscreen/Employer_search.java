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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Employer_search extends AppCompatActivity {

    String userID;
    public RecyclerView mRecyclerView;
    public ExampleAdapter mAdapter;
    public ArrayList<ExampleItem> mExampleList;
    EditText employerSearchbutton;
    CharSequence searchE = "";
    FirebaseAuth fAuth;
    FirebaseFirestore db1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_search);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.employer_home);

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
                        return true;
                }
                return false;
            }
        });
        db1 = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        employerSearchbutton = findViewById(R.id.EmployerSearchBar);

        mExampleList = new ArrayList<>();
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setAdapter(mAdapter);


        db1.collection("Employer").document(userID).collection("Employees").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d : list){
                                ExampleItem p = d.toObject(ExampleItem.class);
                                mExampleList.add(p);
                            }
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                });

        employerSearchbutton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
                searchE = s;

            }

            @Override
            public void afterTextChanged(Editable s) {
                }
        });
    }
}