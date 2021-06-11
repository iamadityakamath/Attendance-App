package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

    private String userID;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView.LayoutManager mLayoutManager;
    private ExampleItem exampleItem;
    private String isUser;


    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    private Object ExampleItem;

    public Employer_search(ArrayList<com.example.splashscreen.ExampleItem> mExampleList) {
        this.mExampleList = mExampleList;
    }

    public Employer_search() {
    }

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

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mExampleList = new ArrayList<>();
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setAdapter(mAdapter);

        fstore.collection("Employer").document(userID).collection("Employees").get()
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


        createExampleList();
        buildRecyclerView();

    }

    private void createExampleList() {

    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).getFname();
        mAdapter.notifyItemChanged(position);
    }



    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


}