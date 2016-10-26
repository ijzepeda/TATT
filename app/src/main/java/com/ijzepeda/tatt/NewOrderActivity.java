package com.ijzepeda.tatt;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.M;
import static com.google.android.gms.analytics.internal.zzy.p;


public class NewOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String usermail="fail@mail.com";
    private static String userName="UserName";
    private static String userLastName="UserLastName";
    private static String useruid="FAKEuserUID123";
    private static String uid="FAKEorderUID123";
    private static String orderNo="000123";
    private static String finalTime="60";
    private static String ordenStatus="enviada";
    private static String paymentId="000230916-1";
    private static String vehicleId="CAR-RANDOMKEY-1212";




    public static double speedCityAvg = 50.0d;
    public static double speedHWAvg = 80.0d;
    public static double tresholdTime = 1.1;
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
    TextView userNameTextView;

     Spinner tipoCargaSpinner,vehiculoSpinner,flujoCargaSpinner;
    RadioGroup paymentType;
    boolean puntoA=false,puntoB=false;
    ImageButton puntoEntregaBtn,puntoOrigenBtn;
    int resultado;
    private Order placeOrder;

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
        setContentView(R.layout.neworder_activity_main);
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



        //---------------------------------------------------------

        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        // -- reference to table in database
        databaseRef=database.getReference("Ordenes");
//        databaseRef2=database.getReference("nombre");



//        databaseRef.push().setValue("Hello, World!");
        //---------------------------------------------------------
//        get database
        databaseRootRef=FirebaseDatabase.getInstance().getReference().getRoot();
//        children are chatroom names> ordenes
//todo lo removii de momento       Map<String,Object> map=new HashMap<>();
//        map.put("Orden12","");
//        databaseRootRef.updateChildren(map);
//---------------------------------------------------

        //load SharedPreferences
        loadUserSession();
        placeOrder=new Order();

         puntoOrigenBtn=(ImageButton)findViewById(R.id.puntoOrigenBtn);
         puntoEntregaBtn=(ImageButton)findViewById(R.id.puntoEntregaBtn);
        Button solicitarBtn=(Button)findViewById(R.id.requestBtn);
        Button pickupDateBtn=(Button)findViewById(R.id.pickupDateBtn);
         distanciaTextView= (TextView)findViewById(R.id.distanciaTextView);
        final EditText orderName=(EditText)findViewById(R.id.nombreOrden);
         etaTextView=(TextView)findViewById(R.id.etaResultTextView);
         costoTextView=(TextView)findViewById(R.id.costoTextView);
        paymentType=(RadioGroup)findViewById(R.id.paymentTypeRadioGroup);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        userNameTextView=(TextView) findViewById(R.id.userNameMenu);
//        userNameTextView.setText("loadedName");
////        userNameTextView.setText(Utils.getInstance().getValue(this,"username"));
//        userNameTextView=(TextView) findViewById(R.id.userNameMenu);
//        userNameTextView.setText("loadedName");
//        Menu menu = navigationView.getMenu();
//        MenuItem nav_camara = menu.findItem(R.id.userNameMenu);
//        nav_camara.setTitle("username");
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        userNameTextView = (TextView)header.findViewById(R.id.userNameMenu);
        Log.e("neworder>create","about to getValue from username");
        userNameTextView.setText(userName);
//        userNameTextView.setText(Utils.getInstance().getValue(getApplication(),"username").toString());
        Log.e("neworder>create","gotValue from username:"+Utils.getInstance().getValue(getApplication(),"username").toString());
//        nav_camara.setTitle(Utils.getInstance().getValue(this,"username"));

//
//        navHeaderView= navigationView.inflateHeaderView(R.layout.nav_header_main);
//        tvHeaderName= (TextView) navHeaderView.findViewById(R.id.tvHeaderName);
//        tvHeaderName.setText("Saly");


        tipoCargaSpinner=(Spinner)findViewById(R.id.cargaTextView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_carga, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCargaSpinner.setAdapter(adapter);

         flujoCargaSpinner=(Spinner)findViewById(R.id.flujoCargaSpinner);
        ArrayAdapter<CharSequence> adapterflujo = ArrayAdapter.createFromResource(this,
                R.array.flujo_carga, android.R.layout.simple_spinner_item);
        adapterflujo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flujoCargaSpinner.setAdapter(adapterflujo);

        vehiculoSpinner=(Spinner)findViewById(R.id.vehiculoSpinner);
        ArrayAdapter<CharSequence> adaptervehiculo = ArrayAdapter.createFromResource(this,
                R.array.car_type, android.R.layout.simple_spinner_item);
        adaptervehiculo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehiculoSpinner.setAdapter(adaptervehiculo);


        //Todo: Mucho codigo repetido,hazlo un metodo

vehiculoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       double x= getCost();
        String selectedCarDimensions="";
        //TODO cambiar las dimesiones
        switch (i) {
            case 0:
             selectedCarDimensions="2.50mX4.00m X2.80. 3.5 Toneladas";
             break;
            case 1:
             selectedCarDimensions="2.50m X 10.00m. 12 Toneladas";
             break;
            case 2:
             selectedCarDimensions="10.00m X 2.50m X2.80m. 24 Toneladas";
             break;

//camioneta: "2.50mX4.00m X2.80. 3.5 Toneladas"
//torton "2.50m X 10.00m. 12 Toneladas"
//                tracto5ejes: "10.00m X 2.50m X2.80m. 24 Toneladas"
        }
        ((TextView) findViewById(R.id.dimensiones)).setText(selectedCarDimensions);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
        flujoCargaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                double x=  getCost();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tipoCargaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                double x=  getCost();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        puntoOrigenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PickLocation.class);
                if(puntoA) {
                    intent.putExtra("markerName","Punto de Origen");

                    intent.putExtra("latitude", originLatitude);
                    intent.putExtra("longitude", originLongitude);
                }
                startActivityForResult(intent,RESULT_PUNTO_ORIGEN);
            }
        });
        puntoEntregaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PickLocation.class);
                if(puntoB) {
                    intent.putExtra("markerName","Punto de Destino");
                    intent.putExtra("latitude", destinationLatitude);
                    intent.putExtra("longitude", destinationLongitude);
                }
                startActivityForResult(intent,RESULT_PUNTO_ENTREGA);
            }
        });



        solicitarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder.setOriginLatitude(originLatitude);
                placeOrder.setOriginLongitude(originLongitude);
                placeOrder.setOriginStreet(originStreet);
                placeOrder.setDestinationLatitude(destinationLatitude);
                placeOrder.setDestinationLongitude(destinationLongitude);
                placeOrder.setDestinationStreet(destinationStreet);
                placeOrder.setOrderName(orderName.getText().toString());
                placeOrder.setVehicle(vehiculoSpinner.getSelectedItem().toString());
                placeOrder.setCargoType(tipoCargaSpinner.getSelectedItem().toString());
                placeOrder.setFlowType(flujoCargaSpinner.getSelectedItem().toString());
                placeOrder.setDistance(distancia+"");
                placeOrder.setPaymentType(((RadioButton)findViewById(paymentType.getCheckedRadioButtonId())).getText().toString());
                placeOrder.setEta(""+getETA(distancia));
                placeOrder.setCost(""+getCost());

                //Todo: revisar estos valores hardocoded
                placeOrder.setStatus(ordenStatus);
                placeOrder.setPaymentId(paymentId);
                placeOrder.setUid(useruid);
                placeOrder.setFinalTime(finalTime);
                placeOrder.setMail(usermail);
                placeOrder.setOrderNo(orderNo);
placeOrder.setVehicleId(vehicleId);//get from some kind of algorithm
Log.e("~~~~","order is:"+placeOrder.toString());


                if(puntoA&&puntoB){
                databaseRef.push().setValue(placeOrder);
                Toast.makeText(context, "Orden Enviada", Toast.LENGTH_SHORT).show();
clearFields();
                }


                //read a children [an order, not the list of main tables]
//                String table_name="Ordenes";
//                String temp_key; //a key that is unique and returned from firebase
//                databaseRootRef=FirebaseDatabase.getInstance().getReference().child(table_name);
//                temp_key=databaseRootRef.push().getKey();
//                DatabaseReference record_root=databaseRootRef.child(temp_key);
//                record_root.updateChildren(MAP of key vlaues to the child);//send the new values
                //-------

            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
        pickupDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });


    }
//    private EditText fr
// teEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private void findViewsById() {
//        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
//        fromDateEtxt.setInputType(InputType.TYPE_NULL);
//        fromDateEtxt.requestFocus();
//
//        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
//        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }
    private void setDateTimeField() {
//        fromDateEtxt.setOnClickListener(this);
//        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
//                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

//        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
////                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


//esto es local!!
    private void loadUserSession(){
//todo checar el result task, si regresa el que paso? y como obtener el uid
        Utils utils=Utils.getInstance();
        usermail=utils.getValue(this,"email");
        userName=utils.getValue(this,"username");
        useruid=utils.getValue(this,"uid");
        Log.e("loadUserSession","gotValue from username:"+Utils.getInstance().getValue(getApplication(),"username").toString());
        Log.e("loadUserSession","gotuid from username:"+Utils.getInstance().getValue(getApplication(),"uid").toString());

//        userLastName=utils.getValue(this,"lastName");
//        Toast.makeText(this, "session mail is:"+usermail, Toast.LENGTH_SHORT).show();

    }

public void clearFields(){

    distanciaTextView.setText("");
    etaTextView.setText("");
    costoTextView.setText("$0.0");
    tipoCargaSpinner.setSelection(0);
    vehiculoSpinner.setSelection(0);
    flujoCargaSpinner.setSelection(0);
    final EditText orderName=(EditText)findViewById(R.id.nombreOrden);
    orderName.setText("");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            puntoOrigenBtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorAccent)));
            puntoEntregaBtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorAccent)));

        }

    }

    destinationLatitude=0.0;
    originLatitude=0.0;
    originLongitude=0.0;
    destinationLongitude=0.0;

distancia=0.0;
    getCost();
    costoTextView.setText("$0.0");
    puntoA=false;
    puntoB=false;


}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_PUNTO_ORIGEN) {

               Intent i = data;
                Bundle bundle=i.getExtras();
                LatLng ll = bundle.getParcelable("pin_location");//pin_location.pin_address
                String address=bundle.getString("pin_address");
                 originStreet=address;
                 originLatitude=ll.latitude;
                 originLongitude=ll.longitude;
//                if(originLatitude!=0)
//                Log.e("~~~~~>","Onactivityresult address is:"+address);
                    if(!address.equals("")) {
                        puntoA = true;

//                        originLatitude=0.0;
//                        originLongitude=0.0;
                        if (Build.VERSION.SDK_INT >= M) {
                            puntoOrigenBtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.teal)));
                        }

                    }

        }
        else
        if (requestCode == RESULT_PUNTO_ENTREGA) {
                Intent i = data;
                Bundle bundle=i.getExtras();
                LatLng ll = bundle.getParcelable("pin_location");//pin_location.pin_address
                String address=bundle.getString("pin_address");
                destinationStreet=address;
                destinationLatitude=ll.latitude;
                destinationLongitude=ll.longitude;
//            Log.e("~~~~~>","Onactivityresult address is:"+address);

//            if(originLatitude!=0)
            if(!address.equals("")) {
                puntoB = true;
//                destinationLatitude=0.0;
//                destinationLongitude=0.0;
                if (Build.VERSION.SDK_INT >= M) {
                    puntoEntregaBtn.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.teal)));//setBackgroundColor(context.getColor(R.color.teal));
                }

            }

        }

            if(puntoA&&puntoB){
            resultado=(int)calculateDistance(
                    originLatitude,
                    originLongitude,
                    destinationLatitude,
                    destinationLongitude,
                    "K");
            }
        }
distanciaTextView.setText(resultado+" km");

    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2, String unit)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "M") {
            dist = dist * 0.8684;
        }

        distancia=dist;
        getETA( dist);
        return  dist;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad)
    {
        return (rad * 180.0 / Math.PI);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
finish();
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


/*
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
            Toast.makeText(this,"Regresar al menu",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_historial) {
            Toast.makeText(this,"Ver el historial",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_new_order) {
            Toast.makeText(this,"Nueva Orden",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_settings) {
            Toast.makeText(this,"Ajustes",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"Compartir",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this,"Enviar",Toast.LENGTH_SHORT).show();

        } else if(id==R.id.nav_login){
            Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show();

        }
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/**getETA() return minutes*/
    public double getETA(double kms){
        double eta;
        if(kms<speedCityAvg)
        eta=kms*(1/speedCityAvg*60);
        else
        eta=kms*(1/speedHWAvg*60);
//        eta*=tresholdTime;
//        refresh time
        eta=Math.floor(eta);
        etaTextView.setText(eta<=59?eta+" mins":eta/60+" hr.");
        getCost();
        return eta;
    }


    public double getCost(){
        //distancia[eta?], vehiculo, tipocarga, flujocarga,
//double total=distancia*(vehiculoSpinner.getSelectedItemPosition()==0?10.0:20.0)*(tipoCargaSpinner.getSelectedItemPosition()==0?1.5:2.0)*(flujoCargaSpinner.getSelectedItemPosition()==0?4.0:7.0);
double total=
//        distancia*(vehiculoSpinner.getSelectedItemPosition()==0?10.0:20.0)*(tipoCargaSpinner.getSelectedItemPosition()==0?1.5:2.0)*(flujoCargaSpinner.getSelectedItemPosition()==0?4.0:7.0);
        distancia*(tipoCargaSpinner.getSelectedItemPosition()==0?20.0:10.0);

        total=Math.ceil(total);
        costoTextView.setText("$"+total);

        return total;
    }



}
