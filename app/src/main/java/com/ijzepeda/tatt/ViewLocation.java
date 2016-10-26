package com.ijzepeda.tatt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.R.string.no;
import static com.google.android.gms.analytics.internal.zzy.i;
import static com.google.android.gms.analytics.internal.zzy.s;
import static com.ijzepeda.tatt.R.id.map;

public class ViewLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    LatLng originLocation;
    LatLng destinationLocation;
    LatLng currentVehicleLocation;
    String address = "MXX";
EditText addressEditText;
//    ImageButton searchButton;
    String originMarkerName = "";
    String destinationMarkerName = "";
    String vehicleMarkerName = "";
    String vehicleId;
    Context context;

    //Connect to firebase and listen to vehicles movements
    boolean onmapready = false;
    DatabaseReference firebaseDatabaseRootReference;

    private GoogleApiClient client;

    Map<String,MarkerOptions> markers=new HashMap<>();

    String carName, carLatStr, carLonStr;
    double carLat, carLon;
    boolean carActive;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnLatLng(); //todo: needed to estimate remaining time
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                returnLatLng();//todo: //needed to estimate remaining time

                return super.onOptionsItemSelected(item);
        }
    }


    //Todo: cambiar el layout. bloquear la busqueda
//     leer los puntos del auto, y marcadoresdel bundle
    //poner solo el marcador del auto elegido
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        address = "Mexico";
        destinationLocation = new LatLng(29.440, -199.255);
        currentVehicleLocation= new LatLng(29.440, -199.255);
        originLocation = new LatLng(29.440, -199.255);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        addressEditText = (EditText) findViewById(R.id.nombreOrden);
        addressEditText.setVisibility(View.GONE);
        ((ImageButton)findViewById(R.id.searchButton)).setVisibility(View.GONE);

        if (getIntent().getExtras() != null) {

            destinationMarkerName = getIntent().getStringExtra("destinationMarkerName");
            originMarkerName = getIntent().getStringExtra("originMarkerName");
            destinationLocation = new LatLng(getIntent().getDoubleExtra("destinationLatitude", 0.0), getIntent().getDoubleExtra("destinationLongitude", 0.0));
            originLocation = new LatLng(getIntent().getDoubleExtra("originLatitude", 0.0), getIntent().getDoubleExtra("originLongitude", 0.0));

            vehicleId = getIntent().getStringExtra("vehicleId");
        }


        //Connect to firebase and listen to vehicles movements
//        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Vehicles/"+vehicleId);
Log.e("es~","CAR-RANDOMKEY-1212=="+vehicleId);
        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Vehicles");
//todo: no funciona porque vehicleid no esta dentro de vehiculos, es la base: asi que hayque leer las bases
//        firebaseDatabaseRootReference.orderByChild("vehicleId").limitToFirst(1).equalTo(vehicleId).addChildEventListener(new ChildEventListener() {
        firebaseDatabaseRootReference.orderByChild("vehicleId").limitToFirst(1).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            //return only the attribute that has changed, only, work with datasnapshot.getkey getvalue, so it works better
            //Seems that String s, is the key parent of the modified value. may contain null, if modified is first
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Log.e("~~~","getKey()"+dataSnapshot.getKey());
                Log.e("~~~","getValue()"+dataSnapshot.getValue());
                Log.e("~>>>>>>>","By latitude key"+(double) dataSnapshot.child("latitude").getValue());
                Log.e("~>>>>>>>","By longitude key"+(double) dataSnapshot.child("longitude").getValue());


                createVehiclesMarkers(dataSnapshot,s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    String car;


boolean zoomOnce=false;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setPadding(0,40, 0, 0);
        //create a point in toluca and delete it.
        createMarker(originLocation,"loading");
        mMap.clear();

                    markers.put("0",new MarkerOptions().position(destinationLocation).title(destinationMarkerName));
//            mMap.addMarker
                    markers.put("1",new MarkerOptions().position(originLocation).title(originMarkerName));
Log.e("~~viewlocation~","MArkers de orden"+destinationMarkerName+", "+originMarkerName);
            mMap.clear();
            createMarker(destinationLocation,destinationMarkerName);
            createMarker(originLocation,originMarkerName);

     ///   }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
              return;
        }
        mMap.setMyLocationEnabled(true);

        if(zoomOnce) {
            zoomOnce=true;

            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(originLocation, 14);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(originLocation));
            mMap.animateCamera(zoom);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    public void createMarker(LatLng latLng,String markerName) {
//        selectedLocation = new LatLng(29.440,-199.255);
//        selectedLocation = latLng;
//        mMap.clear();
        MarkerOptions markerOrigen = new MarkerOptions().position(latLng).title(markerName);//.title("Punto de Origen").draggable(true)
        mMap.addMarker(markerOrigen);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

//        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//            @Override
//            public void onCameraMove() {
//                getReverseGeocoding();
//
//            }
//        });
//Create a reverse geolocation> add it to the address edittext.

    }
//ArrayList<MarkerOptions> markers=new ArrayList<>();

    /**Because cant delete an individual marker, I nees to keep a map with all of them
    when the childlistener detects a change, createvehiclemarkers is triggered with the changed value
     it will clean the map, add or update the car marker by their vehicleid based on the databse
     and then the points are going to be repainted.


     TODO: at this moment, when user creates another marker on click, old vehicles are about to be deleted
     this will be intended as refresh area
     */
    public void createVehiclesMarkers(DataSnapshot dataSnapshot, String vehicleId) {
        mMap.clear();

Log.e("!!~~viewlocatin"," createvehiclemarkers just entered");

        carLat=(double) dataSnapshot.child("latitude").getValue();
        carLon=(double) dataSnapshot.child("longitude").getValue();
        carActive=(boolean) dataSnapshot.child("active").getValue();
        carName=(String) dataSnapshot.child("car").getValue();
        vehicleId=(String) dataSnapshot.child("vehicleId").getValue();


        MarkerOptions carMarker = new MarkerOptions().position(new LatLng(carLat, carLon)).title(carName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));//.title("Punto de Origen").draggable(true)
        markers.put("3",carMarker);//(vehicleId,carMarker);//does this update the mapelement?
/*
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            vehicleId = (String) ((DataSnapshot) i.next()).getValue();
            carActive=(boolean) ((DataSnapshot) i.next()).getValue();
            carName = (String) ((DataSnapshot) i.next()).getValue();
            carLat = (double) ((DataSnapshot) i.next()).getValue();
            carLon = (double) ((DataSnapshot) i.next()).getValue();
            Log.e("!!~~viewlocatin",">>>> createvehiclemarkers get one vehicle result:"+carName+" "+carLat);


            MarkerOptions carMarker = new MarkerOptions().position(new LatLng(carLat, carLon)).title(carName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));//.title("Punto de Origen").draggable(true)
           // mMap.addMarker(markerOrigen);
//           if(carActive)
            markers.put("3",carMarker);//(vehicleId,carMarker);//does this update the mapelement?
//            else //no lo elimina del mapa
//            markers.remove(carMarker);

        }*/

//     Add all cars markers
        for (Map.Entry<String, MarkerOptions> entry : markers.entrySet())
            mMap.addMarker(entry.getValue());


    }


    @Override
    public void onBackPressed() {
//        PinLocation pinLocation=new PinLocation(selectedLocation,address);
//
//        Bundle args = new Bundle();
//        args.putParcelable("pin_location",selectedLocation);
//        args.putString("pin_address",address);
//
//
//
//        Intent resultIntent = new Intent();
//       resultIntent.putExtras(args);
//        if (getParent() == null) {
//            setResult(Activity.RESULT_OK, resultIntent);
//        }
//        else {
//            getParent().setResult(Activity.RESULT_OK, resultIntent);
//        }
//         finish();
//        //return the object pinlocation
        Log.e("~~PickLocation", "onbackpressed");

        returnLatLng();
        super.onBackPressed();

    }

    public void returnLatLng() {

  /*      PinLocation pinLocation = new PinLocation(selectedLocation, address);

        Bundle args = new Bundle();
        args.putParcelable("pin_location", selectedLocation);
        args.putString("pin_address", address);


        Intent resultIntent = new Intent();
        resultIntent.putExtras(args);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, resultIntent);
        } else {
            getParent().setResult(Activity.RESULT_OK, resultIntent);
        }
        finish();
        Log.e("~~PickLocation", "returnLATLNG() result_ok:" + Activity.RESULT_OK + ", pin_latitu" + selectedLocation.latitude);
*/
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        createMarker(latLng);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PickLocation Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
    }
}
