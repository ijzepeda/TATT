package com.ijzepeda.tatt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterDriver extends AppCompatActivity {
TextView nameTV,lastNameTV,phoneTV,addressTV,cardTV, billingAddressTV;
    String uid;
    User user;
    //firebase
    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);
        //create user object
nameTV=(TextView)findViewById(R.id.nameTV);
lastNameTV=(TextView)findViewById(R.id.lastNameTV);
phoneTV=(TextView)findViewById(R.id.phoneTV);
addressTV=(TextView)findViewById(R.id.addressTV);
cardTV=(TextView)findViewById(R.id.cardTV);
billingAddressTV=(TextView)findViewById(R.id.billingAddressTV);

        app= FirebaseApp.getInstance();
        database= FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        // -- reference to table in database
        databaseRef=database.getReference("Users");

uid=Utils.getInstance().getValue(getApplication(),"uid");

        ((Button)findViewById(R.id.guardarBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),NewOrderActivity.class);
                /*Utils.getInstance().save(getApplication(),"username",nameTV.getText().toString());
                Utils.getInstance().save(getApplication(),"uid",uid);
//                Utils.getInstance().save(getApplication(),"mail",mail);
Log.e("saveBTN","user id is:"+uid);
                User user=new User(nameTV.getText().toString(),lastNameTV.getText().toString(),phoneTV.getText().toString(),
                        addressTV.getText().toString(),cardTV.getText().toString(),billingAddressTV.getText().toString(),uid,Utils.getInstance().getValue(getApplication(),"email"));

                databaseRef=database.getReference("Users");
                databaseRef.push().setValue(user);

               Utils utils=Utils.getInstance();
                utils.save(getApplication(),nameTV.getText().toString(),"username");
                Utils.getInstance().save(getApplication(),uid,"uid");

//                utils.save(getApplication(),"email",email);
*/


                startActivity(intent);
            }
        });


    }


    public void updateUser(){
        user.setAddress(((TextView)findViewById(R.id.nameTV)).getText().toString());
        user.setBillingAddress(((TextView)findViewById(R.id.billingAddressTV)).getText().toString());
        user.setCard(((TextView)findViewById(R.id.cardTV)).getText().toString());
        user.setLastName(((TextView)findViewById(R.id.lastNameTV)).getText().toString());
        user.setName(((TextView)findViewById(R.id.nameTV)).getText().toString());
        user.setPhone(((TextView)findViewById(R.id.phoneTV)).getText().toString());



        databaseRef.push().setValue(user);

    }
}
