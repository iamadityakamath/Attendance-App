package com.example.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class employee_seeAttendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storagerefrence;
    String UserID;
    StringBuilder data;
    StringBuilder MonthData;
    Spinner employee_getattendance_month_spinner,employee_getattendance_year_spinner ;
    DecimalFormat df = new DecimalFormat("00");

    public ArrayList<String> export_list_date = new ArrayList<>();
    public ArrayList<String> export_list_checkin = new ArrayList<>();
    public ArrayList<String> export_list_checkin_loc = new ArrayList<>();
    public ArrayList<String> export_list_checkin_address = new ArrayList<>();
    public ArrayList<String> export_list_checkout = new ArrayList<>();
    public ArrayList<String> export_list_checkout_loc = new ArrayList<>();
    public ArrayList<String> export_list_checkout_address = new ArrayList<>();
    public ArrayList<Integer> export_list_hours = new ArrayList<>();
    public ArrayList<Integer> export_list_extra_time = new ArrayList<>();

    public ArrayList<String> export_check_list_month = new ArrayList<String>();
    public ArrayList<String> export_check_list_year = new ArrayList<String>();
    String[] x = {"2021", "2022", "2023", "2024", "2025", "2026"};

    LocalDate currentdate = LocalDate.now();
    int currentmonth = currentdate.getMonthValue()-1;
    String selectedMonthPos;
    String selectedYear;
    Double maxMinutes ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_see_attendance);
        employee_getattendance_month_spinner = findViewById(R.id.employee_getattendance_month_spinner);
        employee_getattendance_year_spinner = findViewById(R.id.employee_getattendance_year_spinner);

        data = new StringBuilder();
        MonthData = new StringBuilder();
        Log.d("new_wsbdd", "kmk");

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //admin declaring max working minutes
        storagerefrence = FirebaseStorage.getInstance().getReference();
        DocumentReference db2 = fstore.collection("Admin").document("FGWUYBcerxMb456ecwuIxvbQJ8L2");
        db2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                maxMinutes =  documentSnapshot.getDouble("maxMinutes");
            }
        });

        PopulateSpinnerMonth();
        PopulateSpinnerYear();
        employee_getattendance_month_spinner.setOnItemSelectedListener(this);
        employee_getattendance_year_spinner.setOnItemSelectedListener(this);

        UserID = getIntent().getStringExtra("userID");
        CollectionReference db1 = fstore.collection("users").document(UserID).collection("Daily");
        db1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> clist = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : clist) {
                        getexportdata p = d.toObject(getexportdata.class);
                        export_list_date.add(p.getCheckin_date_());
                        export_list_checkin.add(p.getCheckin_time_1());
                        export_list_checkin_loc.add(p.getCheckin_location_1());
                        export_list_checkout.add(p.getCheckout_time_2());
                        export_list_checkout_loc.add(p.getCheckout_location_2());
                        export_list_hours.add(p.getMinutes());
                        Log.d("export_list_hours", ""+export_list_hours);
                        export_list_checkin_address.add(p.getCheckin_Adress1());
                        export_list_checkout_address.add(p.getCheckout_Adress_2());
                    }

                    for (int i = 0; i < export_list_date.size(); i++) {
                        String[] dateParts = export_list_date.get(i).split("-");
                        String month = dateParts[1];
                        String year = dateParts[2];
                        export_check_list_month.add(month);
                        export_check_list_year.add(year);
                    }
                }
            }
        });
    }

    private void PopulateSpinnerYear() {
        ArrayAdapter<String> yearadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.year_array));
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employee_getattendance_year_spinner.setAdapter(yearadapter);
    }
    private void PopulateSpinnerMonth() {
        ArrayAdapter<CharSequence>  monthadapter = ArrayAdapter.createFromResource(this, R.array.months_array, android.R.layout.simple_spinner_item);
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employee_getattendance_month_spinner.setAdapter(monthadapter);
        employee_getattendance_month_spinner.setSelection(currentmonth);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.employee_getattendance_year_spinner) {
            int year_pose = parent.getSelectedItemPosition();
            selectedYear = x[year_pose];

        }
        else if (parent.getId() == R.id.employee_getattendance_month_spinner) {
            int c = (int) parent.getSelectedItemPosition() + 1;
            selectedMonthPos = df.format(c); // 0009

        }
    }

    public void see_month_attndance(View view) {
        Log.d("selected_item_is", "year "+selectedYear);
        Log.d("selected_item_is", "month "+selectedMonthPos);

        for (int i = 0; i < export_list_hours.size(); i++) {
            if (export_list_hours.get(i) >= maxMinutes) {
                export_list_extra_time.add(export_list_hours.get(i) - maxMinutes.intValue());
            } else {
                export_list_extra_time.add(0);
            }
        }

        Log.d("selected_item_is", "month "+export_check_list_year);
        Log.d("selected_item_is", "year "+export_check_list_month);
        Log.d("selected_item_is_size", "export_check_list_month"+export_list_date.size());
        MonthData.append("Date,Checkin Time,Checkin Location ,Checkout time,Checkout Location , Minutes, Extra Time");
        for (int i = 0; i < export_list_date.size(); i++){
            if(Objects.equals(export_check_list_month.get(i),selectedMonthPos)){
                if (Objects.equals(export_check_list_year.get(i), selectedYear)) {
                    MonthData.append("\n" + export_list_date.get(i) + ", " + export_list_checkin.get(i) + ", "  +"\""+export_list_checkin_address.get(i)+"\""+" ,"+export_list_checkout.get(i) + ", " +"\""+  export_list_checkout_address.get(i)+"\""+ ", "+export_list_hours.get(i) + "," + export_list_extra_time.get(i));
                }
            }
        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("MonthData.csv", Context.MODE_PRIVATE);
            out.write((MonthData.toString()).getBytes());
            out.close();
            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "MonthData.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.splashscreen.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent,"Choose a CSV"));

        }

        catch(Exception e){
            e.printStackTrace();
        }
        finish();

    }






    public void SetyearlyAttendance(View view) {

        for (int i=0;i<export_list_hours.size();i++){
            if(export_list_hours.get(i)>=maxMinutes){
                export_list_extra_time.add(export_list_hours.get(i)-maxMinutes.intValue());
            }
            else{
                export_list_extra_time.add(0);
            }
        }
        data.append("Date,Checkin Time,Checkin Location ,Checkout time,Checkout Location , Minutes, Extra Time");
        for(int i = 0; i<export_list_date.size(); i++){
            data.append("\n" + export_list_date.get(i) + ", " + export_list_checkin.get(i) + ", "  +"\""+export_list_checkin_address.get(i)+"\""+" ,"+export_list_checkout.get(i) + ", " +"\""+  export_list_checkout_address.get(i)+"\""+ ", "+export_list_hours.get(i) + "," + export_list_extra_time.get(i));
        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();
            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.splashscreen.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent,"Choose a CSV"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finish();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.employee_getattendance_year_spinner) {
            int year_pose = parent.getSelectedItemPosition();
            String[] x = {"2021", "2022", "2023", "2024", "2025", "2026"};
            selectedYear = x[year_pose];
        }
        else if (parent.getId() == R.id.employee_getattendance_month_spinner) {
            int c = (int) parent.getSelectedItemPosition() + 1;
            selectedMonthPos = df.format(c); // 0009

        }
    }
}