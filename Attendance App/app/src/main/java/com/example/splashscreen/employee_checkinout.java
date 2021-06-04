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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.DateTime;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class employee_checkinout extends AppCompatActivity {


    TextView latitudeTextView, longitTextView;
    TextView current_date, current_time;
    String geo;
    Button map, checkin, checkout;
    LocalTime CurrentTime;
    static int x = 0,y=0;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    DocumentReference db1;

    LocalTime office_stopTime =  LocalTime.parse("18:00:00") ;     //17:00:00(5pm)
    int max = 960;
    int difference;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_checkinout);

        latitudeTextView = findViewById(R.id.latitude_pos);
        longitTextView = findViewById(R.id.longitude_pos);
        current_date = (TextView) findViewById(R.id.Date_op);
        current_time = (TextView) findViewById(R.id.time_op);
        map = findViewById(R.id.map);
        checkin = findViewById(R.id.Checkin_button);
        checkout = findViewById(R.id.Checkout_button);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
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
        ///Set current time
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        current_time.setText(dateTime);

        ///Store current time in a variable
        CurrentTime = LocalTime.parse(current_time.getText().toString());

        ///Check in
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
                db1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                       if (documentSnapshot.getString("checkin_time_1") == null){
                           Map<String, Object> userinfo = new HashMap<>();
                           userinfo.put("checkin_date_", current_date.getText().toString());
                           userinfo.put("checkin_time_1", current_time.getText().toString());
                           userinfo.put("checkin_location_1", geo);
                           userinfo.put("checkout_time_1", null);
                           userinfo.put("checkout_location_1", null);

                           userinfo.put("checkin_time_2", null);
                           userinfo.put("checkin_location_2", null);
                           userinfo.put("checkout_time_2", null);
                           userinfo.put("checkout_location_2", null);

                           userinfo.put("hours", null);
                           db1.set(userinfo);
                           checkin.setVisibility(View.INVISIBLE);
                           x = 1;
                       }
                       if (documentSnapshot.getString("checkin_time_1") != null){
                           if(x==1){
                               Map<String, Object> userinfo = new HashMap<>();
                               userinfo.put("checkin_time_2", current_time.getText().toString());
                               userinfo.put("checkin_location_2", geo);
                               db1.set(userinfo, SetOptions.merge());
                               checkin.setVisibility(View.INVISIBLE);
                               x = 1;
                           }

                       }

                   }
                });
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
                db1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.getString("checkout_time_1") == null) {
                            Map<String, Object> userinfo = new HashMap<>();
                            userinfo.put("checkout_time_1", current_time.getText().toString());
                            userinfo.put("checkout_location_1", geo);
                            db1.set(userinfo, SetOptions.merge());
                            checkout.setVisibility(View.INVISIBLE);
                            y = 1;
                        }

                        if (documentSnapshot.getString("checkout_time_1") != null) {

                            Map<String, Object> userinfo = new HashMap<>();
                            userinfo.put("checkout_time_2", current_time.getText().toString());
                            userinfo.put("checkout_location_2", geo);
                            db1.set(userinfo, SetOptions.merge());

                            String final_checkin_time = (documentSnapshot.getString("checkin_time_1"));
                            String final_checkout_time = current_time.getText().toString();
                            Log.d("final_checkin_time", " is"+final_checkin_time);
                            Log.d("final_checkout_time", " is"+final_checkout_time);
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                            Date date1 = null;
                            try { date1 = format.parse(final_checkin_time); } catch (ParseException e) { e.printStackTrace(); }
                            Date date2 = null;try { date2 = format.parse(final_checkout_time); } catch (ParseException e) { e.printStackTrace(); }
                            difference = (int) (date2.getTime() - date1.getTime()) / (1000 * 60);

                            Map<String, Object> userinfo1 = new HashMap<>();
                            userinfo1.put("hours", difference);
                            db1.set(userinfo1, SetOptions.merge());

                            checkout.setVisibility(View.INVISIBLE);
                            y = 0;

                        }
                    }
                });
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
                            geo = "geo:" + location.getLatitude() + "," + location.getLongitude();
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
                Log.d("geo location", "location is:" + geo);
                intent.setData(Uri.parse(geo));
                Intent chooser = Intent.createChooser(intent, "Launch Maps");
                startActivity(chooser);
            }
        });
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

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText(mLastLocation.getLatitude() + "");
            longitTextView.setText(mLastLocation.getLongitude() + "");
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();

        db1 = fstore.collection("users").document(userID).collection("Daily").document(current_date.getText().toString());
        db1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(x==0){
//                    checkin.setVisibility(View.VISIBLE);
//                    checkout.setVisibility(View.INVISIBLE);
//                }
//                if(y==0){
//                    checkin.setVisibility(View.INVISIBLE);
//                    checkout.setVisibility(View.VISIBLE);
//                }



//                if (documentSnapshot.getString("checkin_time") != null){
//                    checkin.setVisibility(View.INVISIBLE);
//                };
//                if (documentSnapshot.getString("checkout_time") != null){ checkout.setVisibility(View.INVISIBLE); }
//                if (office_checkout_last.isAfter(finalTime)) {
//                    if (documentSnapshot.getString("checkin_time") == null) {
//                        checkout.setVisibility(View.INVISIBLE);
//                        Map<String, Object> userinfo = new HashMap<>();
//                        userinfo.put("checkin_time", null);
//                        db1.set(userinfo, SetOptions.merge());
//                    }
//                }
            }
        });


    }
}
