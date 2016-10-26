package com.ijzepeda.tatt;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Switch;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Locale;

import static android.R.attr.name;
import static com.google.android.gms.analytics.internal.zzy.s;

public class EmularAuto extends AppCompatActivity {
    Switch switchCar;
    LocationListener locationListener;
    LocationManager locationManager;
    Context context;

    //firebase
    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    private DatabaseReference databaseRef;
    private DatabaseReference databaseRootRef;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emular_auto);
        switchCar = (Switch) findViewById(R.id.switch1);

        //Firebase---------------------------------------------------------
        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        // -- reference to table in database
        databaseRef=database.getReference("Vehicles/CAR-RANDOMKEY-1212");
//        databaseRootRef=FirebaseDatabase.getInstance().getReference().getRoot();

        carUpdatePos=new Vehicle(true,"camioneta-1",19.3205464,-99.624662,"vehicle123");


        //Location------------------------------------
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.e("EmularAuto", "2oncreate");

        if (location != null) {
            Log.e("EmularAuto", "3oncreate");

            showMyAddress(location);
        }
// Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                showMyAddress(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        if (switchCar.isActivated()){
// Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }else{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
            locationManager.removeUpdates(locationListener);}

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //added to avoid battery waste
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   return;
        }
        locationManager.removeUpdates(locationListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);//todo delete maybe
    }

    Vehicle carUpdatePos;
    private void showMyAddress(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.e("EmularAuto","showMyAddress.Me muevo y esta es mi latitud"+latitude+" esta es la longaniza:"+longitude);
 carUpdatePos.setLatitude(latitude);
 carUpdatePos.setLongitude(longitude);

//        databaseRef.push().setValue(carUpdatePos);
        databaseRef.setValue(carUpdatePos);
//databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//
//    }
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//
//    }
//});
//        databaseRef.child("car").setValue("COCHETON");
        //The following 2 function calls are equivalent
//        fredRef.update({ name: { first: 'Fred', last: 'Flintstone' }});
//        fredRef.child('name').set({ first: 'Fred', last: 'Flintstone' });


    }




}
