package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.DateTime;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class employee_checkinout extends AppCompatActivity {


    TextView latitudeTextView, longitTextView,LocationAdress,current_date,current_time;
    String OfficeStartTime,OfficeStopTime ;
    Double latitude,Longitude;
    Double maxMinutes ;
    int offstarttime,offstoptime,difference;

    static int vcin=1,vcout=0;
    static  int view_buttons=0;

    Geocoder geocoder;
    List<Address> addresses;
    String geo,full_address;
    Button map, checkin, checkout;
    LocalTime CurrentTime;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    DocumentReference db1;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_checkinout);



        latitudeTextView = findViewById(R.id.latitude_pos);
        longitTextView = findViewById(R.id.longitude_pos);
        current_date = (TextView) findViewById(R.id.Date_op);
        current_time = (TextView) findViewById(R.id.time_op);
        LocationAdress = findViewById(R.id.Locationtxt_input);
        map = findViewById(R.id.map);
        checkin = findViewById(R.id.Checkin_button);
        checkout = findViewById(R.id.Checkout_button);
        checkin.setVisibility(View.INVISIBLE);
        checkout.setVisibility(View.INVISIBLE);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        //get office start and stopTime
        DocumentReference db2 = fstore.collection("Admin").document("FGWUYBcerxMb456ecwuIxvbQJ8L2");
        db2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                OfficeStartTime =documentSnapshot.getString("OfficeStartTime");
                OfficeStopTime = documentSnapshot.getString("OfficeStopTime");
                maxMinutes =  documentSnapshot.getDouble("maxMinutes");
                String[] y = OfficeStartTime.split(":");
                offstarttime = (Integer.parseInt(y[0])*3600  + Integer.parseInt(y[1])*60  + Integer.parseInt(y[2]) );
                String[] z =OfficeStopTime.split(":");
                offstoptime = (Integer.parseInt(z[0])*3600  + Integer.parseInt(z[1])*60  + Integer.parseInt(z[2]) );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(employee_checkinout.this, "Office start and stop time not defined", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (employee_checkinout.this, employee_home.class);
                startActivity(intent);
            }
        });

        //  get the location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ///Variable for date and time
        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();

        ///Set current date
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        current_date.setText(dateTime);
//        current_date.setText("27-06-2021");

        ///Set current time
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        current_time.setText(dateTime);
        CurrentTime = LocalTime.parse(current_time.getText().toString());

        ///Convert office start,stop and current time to int
        String[] x = CurrentTime.toString().split(":");
        int currtime = (Integer.parseInt(x[0])*3600  + Integer.parseInt(x[1])*60  + Integer.parseInt(x[2]) );

//

        db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
        db1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("checkin_time_1") == null) {
                    checkin.setVisibility(View.VISIBLE);
                    checkout.setVisibility(View.INVISIBLE);
                }
                if (documentSnapshot.getString("checkin_time_1") != null) {
                    checkin.setVisibility(View.INVISIBLE);
                    checkout.setVisibility(View.VISIBLE);
                }
                if ((documentSnapshot.getString("checkin_time_1") != null) && documentSnapshot.getString("checkout_time_1")!=null){
                    if(view_buttons==0){
                        checkin.setVisibility(View.VISIBLE);
                        checkout.setVisibility(View.INVISIBLE);
                    }
                    if (view_buttons ==1){
                        checkin.setVisibility(View.INVISIBLE);
                        checkout.setVisibility(View.VISIBLE);
                    }
                }
//                if(currtime>offstoptime || currtime<offstarttime){
//                    checkin.setVisibility(View.INVISIBLE);
//                }
            }
        });



        //Check in
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vcin = 0;
                vcout = 1;
                db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
                db1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("checkin_time_1") == null) {
                            Map<String, Object> userinfo = new HashMap<>();
                            userinfo.put("checkin_date_", current_date.getText().toString());

                            userinfo.put("checkin_time_1", current_time.getText().toString());
                            userinfo.put("checkin_location_1", geo);
                            userinfo.put("checkin_Adress1", full_address);

                            userinfo.put("checkin_time_2", null);
//                            userinfo.put("checkin_location_2", null);
//                            userinfo.put("checkin_Adress_2", null);

                            userinfo.put("checkout_time_1", null);
//                            userinfo.put("checkout_location_1", null);
//                            userinfo.put("checkin_Adress1", null);

                            userinfo.put("checkout_time_2", null);
                            userinfo.put("checkout_location_2", null);
                            userinfo.put("checkout_Adress_2", null);
                            userinfo.put("minutes", 0);
                            db1.set(userinfo);
                        }
                        if (documentSnapshot.getString("checkin_time_1") != null ) {
                            Map<String, Object> userinfo = new HashMap<>();
                            userinfo.put("checkin_time_2", current_time.getText().toString());
//                            userinfo.put("checkin_location_2", geo);
//                            userinfo.put("checkin_Adress2", full_address);
                            db1.set(userinfo, SetOptions.merge());
                        }
                    }
                });
                view_buttons=1;
                Intent intent = new Intent (employee_checkinout.this, employee_home.class);
                startActivity(intent);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vcin = 1;
                vcout = 0;

                db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
                db1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.getString("checkout_time_1") == null) {
                            String checkin_time_1 = documentSnapshot.getString("checkin_time_1");
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                            Date time1 = null; try {  time1 = format.parse(checkin_time_1); } catch (ParseException e) { e.printStackTrace(); }
                            Date time2 = null; try {  time2 = format.parse(current_time.getText().toString()); } catch (ParseException e) { e.printStackTrace(); }
                            difference = (int) (time2.getTime() - time1.getTime()) / (1000 * 60);

                            Map<String, Object> userinfo = new HashMap<>();
                            userinfo.put("checkout_time_1", current_time.getText().toString());
//                            userinfo.put("checkout_location_1", geo);
//                            userinfo.put("checkout_Adress1", full_address);
                            userinfo.put("minutes",difference);
                            db1.set(userinfo, SetOptions.merge());

                        }

                        if (documentSnapshot.getString("checkout_time_1") != null) {

                            String checkin_time_2 = documentSnapshot.getString("checkin_time_2");
                            Long prevminutes = documentSnapshot.getLong("minutes");
                            Log.d("enter_long_mi", ""+prevminutes);

                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                            Date time1 = null; try {  time1 = format.parse(checkin_time_2); } catch (ParseException e) { e.printStackTrace(); }
                            Date time2 = null; try {  time2 = format.parse(current_time.getText().toString()); } catch (ParseException e) { e.printStackTrace(); }
                            difference = (int) ( (int)((time2.getTime() - time1.getTime()) / (1000 * 60)) + prevminutes.intValue());

                            Map<String, Object> userinfo = new HashMap<>();
                            userinfo.put("checkout_time_2", current_time.getText().toString());
                            userinfo.put("checkout_location_2", geo);
                            userinfo.put("checkout_Adress_2", full_address);
                            if(difference>maxMinutes){
                                userinfo.put("minutes",maxMinutes);
                            }
                            else{
                                userinfo.put("minutes",difference);
                            }
                            db1.set(userinfo, SetOptions.merge());
                        }
                    }
                });
                view_buttons=0;
                checkout.setVisibility(View.INVISIBLE);
                Intent intent = new Intent (employee_checkinout.this, employee_home.class);
                startActivity(intent);
            }

        });

    }


    //Location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");
                            latitude = location.getLatitude();
                            Longitude = location.getLongitude();
                            if(latitude!=0.0 && Longitude!=0.0){
                                GeocodeAdress();
                            }


                            geo = "geo:" + location.getLatitude() + " , " + location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }

        /// Open Maps for the selected coordinates
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((Intent.ACTION_VIEW));
                intent.setData(Uri.parse(geo));
                Intent chooser = Intent.createChooser(intent, "Launch Maps");
                startActivity(chooser);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void GeocodeAdress() {
        geocoder = new Geocoder(employee_checkinout.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(19.2072513,72.8733597,1);
            Log.d("addresess_is", ""+addresses);
            String address = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalcode = addresses.get(0).getPostalCode();
            full_address = address;
            LocationAdress.setText(full_address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText(mLastLocation.getLatitude() + "");
            longitTextView.setText(mLastLocation.getLongitude() + "");
            latitude = mLastLocation.getLatitude();
            Longitude = mLastLocation.getLongitude();
            GeocodeAdress();
        }
    };
    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


    }
    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }
    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
///ON START
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
//        db1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                Log.d("valueof", "value of x is "+x);
////                Log.d("valueof", "value of y is "+y);
////                if (x == 0 ) {
////                    checkin.setVisibility(View.VISIBLE);
////                    checkout.setVisibility(View.INVISIBLE);
////                    x = 1;
////                }
////                if (y == 0 ) {
////                    checkin.setVisibility(View.INVISIBLE);
////                    checkout.setVisibility(View.VISIBLE);
////                    y = 1;
////                }
//            }
//        });
//    }

    ///Go to different pages
    public void employee_checkinout_to_home(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_home.class);
        startActivity(intent);
    }
    public void employee_checkinout_to_profile(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_profile.class);
        startActivity(intent);
    }
    public void employee_checkinout_to_calendar(View v) {
        //Toast.makeText(this, "Check in/out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, employee_calendar.class);
        startActivity(intent);
    }



}
