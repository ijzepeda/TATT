package com.ijzepeda.tatt;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.m;
import static com.google.android.gms.analytics.internal.zzy.r;
import static com.ijzepeda.tatt.R.id.costoTextView;
import static com.ijzepeda.tatt.R.id.distanciaTextView;
import static com.ijzepeda.tatt.R.id.vehiculoTextView;

public class OrdenesActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private static String usermail="";
    private static String uid="FAKEUID123";
    Context context;
    private RecyclerView ordersRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private OrdersRecyclerViewAdapter recyclerAdapter;
    //firebase
    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private DatabaseReference databaseRef;
    private DatabaseReference databaseRootRef;
    private StorageReference storageRef;
    DatabaseReference firebaseDatabaseRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);
        //LOAD USER SESSION
        loadUserSession();
        if(usermail.equals("")){
            Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return;
        }

//Toolbar, GUI , DRAWER
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=getApplication();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Version Demo", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //RecyclerView
        ordersRecyclerView=(RecyclerView)findViewById(R.id.orders_recyclerview);
        linearLayoutManager=new LinearLayoutManager(this);

        //FIREBASE
        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        // -- reference to table in database
        databaseRef=database.getReference("Ordenes");

//        get database
        databaseRootRef=FirebaseDatabase.getInstance().getReference().getRoot();
//        children are chatroom names> ordenes
//        Map<String,Object> map=new HashMap<>();//TODO:this also works
//        map.put("Orden12","");//TODO:this also works
//        databaseRootRef.updateChildren(map);//TODO:this also works

//        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Vehicles/CAR-RANDOMKEY-1212");
//        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Ordenes");
        firebaseDatabaseRootReference = FirebaseDatabase.getInstance().getReference().child("Ordenes");
///        firebaseDatabaseRootReference.orderByChild("mail").limitToFirst(1).equalTo(usermail).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            //limitToLast(10).
        firebaseDatabaseRootReference.orderByChild("mail").equalTo(usermail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Log.d("ordenesA,User key", childSnapshot.getKey());//  D/User key: -KSZqD6W_kjmOPKwh3i8
                    Log.d("ordenesA,User ref", childSnapshot.getRef().toString());//   D/User ref: https://tatt-5dc00.firebaseio.com/Ordenes/-KSZqD6W_kjmOPKwh3i8
                    Log.d("ordenesA,User val", childSnapshot.getValue().toString()); //< Contains the whole json:.
                     String name = (String) childSnapshot.child("name").getValue();
                    String message = (String) childSnapshot.child("message").getValue();
Log.e("~~ORdenes","ORden:"+((String)childSnapshot.child("originStreet").getValue())+", orderName:"+((String)childSnapshot.child("orderName").getValue()));
//                    orderName.setText((String) childSnapshot.child("mail").getValue());
//Order orderTemp=new Order();
//                    Message message = messageSnapshot.getValue(Message.class);

//todo                    Order orderTemp= dataSnapshot.getValue(Order.class); //Incluye el referenceID<<
                    Order orderTemp= dataSnapshot.child(childSnapshot.getKey()).getValue(Order.class); //Incluye el referenceID<<
                    orderTemp.setOrderNo(childSnapshot.getKey());//todo actualizar su orderNo, al id de orden>
                    ordersList.add(orderTemp);
                    Log.e("ordenesActivity", "Agregue orderTemp a ordersList, esto tieneorderTemp:"+orderTemp.toString());
                    Log.e("ordenesActivity", "Agregue orderTemp a ordersList, orderslist tiene:"+ordersList.size());
                  }



//Outside this method wont work
                ordersRecyclerView.setLayoutManager(linearLayoutManager);
                recyclerAdapter=new OrdersRecyclerViewAdapter(ordersList);
                Log.e("ordenesActivity", "cuando meta la lista al adaptador:"+ordersList.size());
                recyclerAdapter.notifyDataSetChanged();
                ordersRecyclerView.setAdapter(recyclerAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    List<Order> ordersList=new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();

//        if (List.size() == 0) {
//            llenar;
//        }
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
