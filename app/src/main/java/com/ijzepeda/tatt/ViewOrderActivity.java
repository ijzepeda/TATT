package com.ijzepeda.tatt;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;
import static com.google.android.gms.analytics.internal.zzy.C;
import static com.google.android.gms.analytics.internal.zzy.r;
import static com.ijzepeda.tatt.R.id.map;
import static com.ijzepeda.tatt.R.id.puntoEntregaBtn;


public class ViewOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static double speedCityAvg = 50.0d;
    public static double speedHWAvg = 80.0d;
    public static double tresholdTime = 1.1;
    private static String usermail="remove@hardcodemail.com";
    private static String uid="FAKEUID123";
    private static String orderNo="000123";
    private static String finalTime="60";
//    private static String vehicleId="CAR-RANDOMKEY-1212";
    private static String ordenStatus="enviada";
    private static String paymentId="000230916-1";

    private static int RESULT_PUNTO_ORIGEN=1;
    private static int RESULT_PUNTO_ENTREGA=2;
    Context context;
    String originStreet;
    double originLatitude;
    double originLongitude;
    String destinationStreet;
    double destinationLatitude;
    double destinationLongitude;
double distancia;
    TextView distanciaTextView;
    TextView etaTextView;
    TextView costoTextView;
    TextView orderName;
     Spinner tipoCargaSpinner,vehiculoSpinner,flujoCargaSpinner;
    TextView paymentType;
    boolean puntoA=false,puntoB=false;
    ImageButton viewMapBtn;
    TextView flujoCarga,tipoCarga,vehiculoTextView;


    int resultado;

    DatabaseReference firebaseDatabaseRootReference;
    private String vehicleId;
    private Order placeOrder;
    String orderUID;

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
        setContentView(R.layout.vieworder_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
 context=getApplication();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Version Demo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        placeOrder=new Order();
        //LoadSharedPreferences
        loadUserSession();
        //LOAD THE UID from the extras
        orderUID=getIntent().getExtras().get("orderUID").toString();


//        puntoOrigenBtn=(ImageButton)findViewById(R.id.puntoOrigenBtn);
        viewMapBtn=(ImageButton)findViewById(R.id.viewMapBtn);
        Button cancelarBtn=(Button)findViewById(R.id.cancelarBtn);
        distanciaTextView= (TextView)findViewById(R.id.distanciaTextView);
        orderName=(TextView)findViewById(R.id.nombreOrden);
        etaTextView=(TextView)findViewById(R.id.etaResultTextView);
        costoTextView=(TextView)findViewById(R.id.costoTextView);
        paymentType=(TextView)findViewById(R.id.tipoPagoResultTextView);
         flujoCarga=(TextView)findViewById(R.id.flujoCargaResulTextView);
         tipoCarga=(TextView)findViewById(R.id.tipoCargaResultTextView);
        vehiculoTextView=(TextView)findViewById(R.id.vehiculoResultTextView);
        //---------------------------------------------------------

        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        // -- reference to table in database
        databaseRef=database.getReference("Ordenes");

        //---------------------------------------------------------
//        get database
        databaseRootRef=FirebaseDatabase.getInstance().getReference().getRoot();
//        children are chatroom names> ordenes
//        Map<String,Object> map=new HashMap<>();//TODO:this also works
//        map.put("Orden12","");//TODO:this also works
//        databaseRootRef.updateChildren(map);//TODO:this also works

//        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Vehicles/CAR-RANDOMKEY-1212");
        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Ordenes"+"/"+orderUID);//"/-KSZqD6W_kjmOPKwh3i8");
Log.e("~~>>>","El objeto en base a referencia:"+orderUID);
//        firebaseDatabaseRootReference.getKey().equals("-KSZqD6W_kjmOPKwh3i8").addListenerForSingleValueEvent(new ValueEventListener() {
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

//        firebaseDatabaseRootReference.orderByChild("mail").limitToFirst(1).equalTo(usermail).addListenerForSingleValueEvent(new ValueEventListener() {
        firebaseDatabaseRootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("ondatachange","<>"+dataSnapshot.getValue(Order.class).toString());
                Order order=  dataSnapshot.getValue(Order.class);
                orderName.setText(order.getOrderName());
                distanciaTextView.setText(order.getDistance().substring(0,(order.getDistance().indexOf("."))+2)+" km.");
                etaTextView.setText(order.getEta());//(Integer.getInteger(order.getEta())>59)?""+(Integer.getInteger(order.getEta())/60):order.getEta());
                costoTextView.setText("$"+order.getCost());
                paymentType.setText(order.getPaymentType());
                flujoCarga.setText(order.getFlowType());
                tipoCarga.setText(order.getCargoType());
                vehiculoTextView.setText(order.getVehicle());
//                    .setText((String) childSnapshot.child("").getValue());
                originStreet=order.getOriginStreet();
                originLatitude=order.getOriginLatitude();
                originLongitude=order.getOriginLongitude();
                destinationStreet=order.getDestinationStreet();
                destinationLatitude=order.getDestinationLatitude();
                destinationLongitude=order.getDestinationLongitude();
                vehicleId=order.getVehicleId(); //camioneta123
                /**for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Log.d(">User key", childSnapshot.getKey());//  D/User key: -KSZqD6W_kjmOPKwh3i8

                    Log.d(">User ref", childSnapshot.getRef().toString());//   D/User ref: https://tatt-5dc00.firebaseio.com/Ordenes/-KSZqD6W_kjmOPKwh3i8

                    Log.d(">User val", childSnapshot.getValue().toString()); //< Contains the whole json:.
//                    D/User val: {paymentId=000230916-1, paymentType=Tarjeta C/D,
// destinationLatitude=19.291014176484435, mail=remove@hardcodemail.com, finalTime=60,
// originLongitude=-99.5795825868845, orderNo=000123, cargoType=general,
// originLatitude=19.383919698762014, status=enviada, orderName=D, eta=12.0,
// destinationLongitude=-99.5997913554311, vehicle=Camioneta, flowType=Turistico,
// uid=FAKEUID123, destinationStreet=Delegación Santa María Totoltepec, Toluca de Lerdo,
// distance=10.545468631772223, cost=633.0, originStreet=Avenida Toluca 151, Villa Cuauhtémoc}
                            String name = (String) childSnapshot.child("name").getValue();
                            String message = (String) childSnapshot.child("message").getValue();


                    orderName.setText((String) childSnapshot.child("mail").getValue());
                    distanciaTextView.setText((String) childSnapshot.child("distance").getValue());
                    etaTextView.setText((String) childSnapshot.child("eta").getValue());
                    costoTextView.setText((String) childSnapshot.child("cost").getValue());
                    paymentType.setText((String) childSnapshot.child("paymentType").getValue());
                    flujoCarga.setText((String) childSnapshot.child("flowType").getValue());
                    tipoCarga.setText((String) childSnapshot.child("cargoType").getValue());
                    vehiculoTextView.setText((String) childSnapshot.child("vehicle").getValue());
//                    .setText((String) childSnapshot.child("").getValue());
                     originStreet=(String)childSnapshot.child("originStreet").getValue();
                     originLatitude=(double)childSnapshot.child("originLatitude").getValue();
                     originLongitude=(double)childSnapshot.child("originLongitude").getValue();
                     destinationStreet=(String)childSnapshot.child("destinationStreet").getValue();
                     destinationLatitude=(double)childSnapshot.child("destinationLatitude").getValue();
                     destinationLongitude=(double)childSnapshot.child("destinationLongitude").getValue();
                    vehicleId=(String)childSnapshot.child("vehicleId").getValue();
Log.e("vieworder","onclick map with latitudes:"+originLatitude+" , "+destinationLatitude);
                }
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//---------------------------------------------------



        //-----LoadValues from Database

        //read a children [an order, not the list of main tables]
//                String table_name="Ordenes";
//                String temp_key; //a key that is unique and returned from firebase
//                databaseRootRef=FirebaseDatabase.getInstance().getReference().child(table_name);
//                temp_key=databaseRootRef.push().getKey();
//                DatabaseReference record_root=databaseRootRef.child(temp_key);
//                record_root.updateChildren(MAP of key vlaues to the child);//send the new values
        //-------


        //Todo: Mucho codigo repetido,hazlo un metodo


        viewMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewLocation.class);
//                if(puntoB) {
                    intent.putExtra("destinationMarkerName","Punto de Destino");
                    intent.putExtra("originMarkerName","Punto de Entrega");
                    intent.putExtra("destinationLatitude", destinationLatitude);
                    intent.putExtra("destinationLongitude", destinationLongitude);
                    intent.putExtra("originLatitude", originLatitude);
                    intent.putExtra("originLongitude", originLongitude);
                    intent.putExtra("vehicleId", vehicleId);
//                    Log.e("~vieworder~~","callingss:"+vehicleId+" viewlocation"+destinationLatitude+", "+originLongitude);

//                }
                startActivityForResult(intent,RESULT_PUNTO_ENTREGA);
            }
        });



        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewOrderActivity.this);
                alertDialog
                        .setTitle("Cancelar Orden")
                        .setMessage("Quieres cancelar la orden?")
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.show();

            }
        });

/*
        solicitarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder.setOriginLatitude(originLatitude);
                placeOrder.setOriginLongitude(originLongitude);
                placeOrder.setOriginStreet(originStreet);
                placeOrder.setDestinationLatitude(destinationLatitude);
                placeOrder.setDestinationLongitude(destinationLongitude);
                placeOrder.setDestinationStreet(destinationStreet);
//                placeOrder.setOrderName(orderName.getText().toString());
                placeOrder.setVehicle(vehiculoSpinner.getSelectedItem().toString());
                placeOrder.setCargoType(tipoCargaSpinner.getSelectedItem().toString());
                placeOrder.setFlowType(flujoCargaSpinner.getSelectedItem().toString());
                placeOrder.setDistance(distancia+"");
                placeOrder.setPaymentType(((RadioButton)findViewById(paymentType.getCheckedRadioButtonId())).getText().toString());
//                placeOrder.setEta(""+getETA(distancia));
//                placeOrder.setCost(""+getCost());

                //Todo: revisar estos valores hardocoded
                placeOrder.setStatus(ordenStatus);
                placeOrder.setPaymentId(paymentId);
                placeOrder.setUid(uid);
                placeOrder.setFinalTime(finalTime);
                placeOrder.setMail(usermail);
                placeOrder.setOrderNo(orderNo);

Log.e("~~~~","order is:"+placeOrder.toString());
                databaseRef.push().setValue(placeOrder);
//                databaseRootRef.updateChildren(map//[childvalue,object]);//TODO:this also works


            }
        });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_PUNTO_ORIGEN) {

//                if (Build.VERSION.SDK_INT >= M) {
//                    puntoOrigenBtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.teal)));
//                }
//               Intent i = data;
//                Bundle bundle=i.getExtras();
//                LatLng ll = bundle.getParcelable("pin_location");//pin_location.pin_address
//                String address=bundle.getString("pin_address");
//                 originStreet=address;
//                 originLatitude=ll.latitude;
//                 originLongitude=ll.longitude;
//                if(originLatitude!=0)
//                puntoA=true;

        }
        else
        if (requestCode == RESULT_PUNTO_ENTREGA) {
//            if (Build.VERSION.SDK_INT >= M) {
//                puntoEntregaBtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.teal)));//setBackgroundColor(context.getColor(R.color.teal));
//            }
//                Intent i = data;
//                Bundle bundle=i.getExtras();
//                LatLng ll = bundle.getParcelable("pin_location");//pin_location.pin_address
//                String address=bundle.getString("pin_address");
//                destinationStreet=address;
//                destinationLatitude=ll.latitude;
//                destinationLongitude=ll.longitude;
//            if(originLatitude!=0)
//                puntoB=true;


        }

//            if(puntoA&&puntoB){
//            resultado=(int)calculateDistance();
//
//            }
        }
//////////distanciaTextView.setText(resultado+" km");// todo: resultado de que?

    }

    //esto es local!!
    private void loadUserSession(){
//todo checar el result task, si regresa el que paso? y como obtener el uid
        Utils utils=Utils.getInstance();
        usermail=utils.getValue(this,"email");
//        Toast.makeText(this, "session mail is:"+usermail, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Menuoptions mo=new Menuoptions();
        mo.menuAction(item,getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







}
