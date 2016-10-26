package com.ijzepeda.tatt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.attr.direction;
import static com.google.android.gms.analytics.internal.zzy.m;
import static com.ijzepeda.tatt.R.id.map;

public class PickLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    LatLng selectedLocation;
    String address="";
    EditText addressEditText;
    ImageButton searchButton;
    String markerName = "aqui";
    Context context;
    boolean isMarkerPlaced=false;

    //Connect to firebase and listen to vehicles movements
    boolean onmapready = false;
    DatabaseReference firebaseDatabaseRootReference;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnLatLng();
                Log.e("~~PickLocation", "optionitemselected:home");

                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                returnLatLng();
                Log.e("~~PickLocation", "optionitemselected:default");

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        address = "Mexico";
       // selectedLocation = new LatLng(29.440, -199.255);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        addressEditText = (EditText) findViewById(R.id.nombreOrden);
        searchButton = (ImageButton) findViewById(R.id.searchButton);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String direccion = addressEditText.getText().toString();

                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addressList;
//                GeoPoint p1 = null;

                //Hide Soft Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
if(!direccion.isEmpty() && !direccion.equals("") && !direccion.equals(" ")) {
    try {
        addressList = coder.getFromLocationName(direccion, 5);

        if (addressList != null) {
            Address location = addressList.get(0);
            location.getLatitude();
            location.getLongitude();
LatLng latlng=new LatLng(location.getLatitude(), location.getLongitude());
            selectedLocation = latlng;
             address=direccion;
            mMap.clear();
            createMarker(latlng);

        }
    } catch (IOException e) {
        e.printStackTrace();
    }

}else{
    ;//not empty
}
            }
        });



//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//
//            @Override
//            public void onMyLocationChange(Location arg0) {
//                // TODO Auto-generated method stub
//
//                mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
//            }
//        });


        //Connect to firebase and listen to vehicles movements

        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Vehicles");
        firebaseDatabaseRootReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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


    public void getReverseGeocoding() {
        Geocoder geoCoder = new Geocoder(this);
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(selectedLocation.latitude, selectedLocation.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(matches!=null) {
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            if (bestMatch != null) {
//       address=(!bestMatch.getFeatureName().isEmpty()?bestMatch.getFeatureName()+", ":"")+
                address = (bestMatch.getAddressLine(0) != null && !bestMatch.getAddressLine(0).isEmpty() ? bestMatch.getAddressLine(0) : "") + ", "
                        + (bestMatch.getLocality() != null && !bestMatch.getLocality().isEmpty() ? bestMatch.getLocality() : "");
            }
            addressEditText.setText(address);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      mMap.getUiSettings().setMapToolbarEnabled(false);
//

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectedLocation = latLng;
                mMap.clear();
                createMarker(latLng);
                getReverseGeocoding();
                isMarkerPlaced=true;
            }
        });
        mMap.setPadding(0,140, 0, 0);

        if (getIntent().getExtras() != null) {
            markerName = getIntent().getStringExtra("markerName");
            Log.e("~", "markername" + markerName + getIntent().getDoubleExtra("latitude", 29.44));
            selectedLocation = new LatLng(getIntent().getDoubleExtra("latitude", 29.440), getIntent().getDoubleExtra("longitude", -199.255));
            mMap.clear();
            createMarker(selectedLocation);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

boolean zoomonce=false;
    public void createMarker(LatLng latLng) {
        Log.e("CreateMarker", "about to create a marker"+markerName+" at:"+latLng.latitude);
//        selectedLocation = new LatLng(29.440,-199.255);
        selectedLocation = latLng;
//        mMap.clear();
        MarkerOptions markerOrigen = new MarkerOptions().position(selectedLocation).title(markerName);//.title("Punto de Origen").draggable(true)
        mMap.addMarker(markerOrigen);

        if(zoomonce==false) {
            zoomonce=true;
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
            mMap.animateCamera(zoom);
        }

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
    Map<String,MarkerOptions> markers=new HashMap<>();

    String carName,carId, carLatStr, carLonStr;
    double carLat, carLon;
    boolean carActive;
    /**Because cant delete an individual marker, I nees to keep a map with all of them
    when the childlistener detects a change, createvehiclemarkers is triggered with the changed value
     it will clean the map, add or update the car marker by their vehicleid based on the databse
     and then the points are going to be repainted.


     TODO: at this moment, when user creates another marker on click, old vehicles are about to be deleted
     this will be intended as refresh area
     */
    public void createVehiclesMarkers(DataSnapshot dataSnapshot, String vehicleId) {
        mMap.clear(); //todo no need for this, if car moves, and map was clicked,then it is ging to be erased
        Log.e("createVehiclesMarkers", "about to create avehiclemarker"+markerName+" at:");


        if (selectedLocation != null)
            createMarker(selectedLocation);
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {

            carActive=(boolean) ((DataSnapshot) i.next()).getValue();
            carName = (String) ((DataSnapshot) i.next()).getValue();
            carLat = (double) ((DataSnapshot) i.next()).getValue();
            carLon = (double) ((DataSnapshot) i.next()).getValue();
            carId = (String) ((DataSnapshot) i.next()).getValue();


            MarkerOptions carMarker = new MarkerOptions().position(new LatLng(carLat, carLon)).title(carName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));//.title("Punto de Origen").draggable(true)
           // mMap.addMarker(markerOrigen);
           if(carActive)
            markers.put(vehicleId,carMarker);//does this update the mapelement?
            else //no lo elimina del mapa
            markers.remove(carMarker);

        }

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
        if(isMarkerPlaced) {
            PinLocation pinLocation = new PinLocation(selectedLocation, address);

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
        }else{
            Intent resultIntent = new Intent();
//            resultIntent.putExtras(args);
            if (getParent() == null) {
                setResult(Activity.RESULT_CANCELED, resultIntent);
            } else {
                getParent().setResult(Activity.RESULT_CANCELED, resultIntent);
            }

        }
        finish();

    }

    @Override
    public void onMapClick(LatLng latLng) {
        createMarker(latLng);
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
