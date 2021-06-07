package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
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
    public Admin_Employer items = null;

    FirebaseAuth asefAuth;
    FirebaseFirestore asedb;


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

        adminEmployeeList = new ArrayList<>();
        aseAdapter = new AdminEmployeeAdapter(this, adminEmployeeList);

        aseRecyclerView.setAdapter(aseAdapter);

        asedb.collection("Employer").document(getIntent().getStringExtra("employeruserID")).collection("Employees").get()
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

    }
}