package com.ijzepeda.tatt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.google.android.gms.analytics.internal.zzy.p;

/**
 * Created by Ivan on 9/21/2016.
 */

public class Menuoptions {

    public void menuAction(MenuItem item, Context context){
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
//            Toast.makeText(context,"Regresar al menu",Toast.LENGTH_SHORT).show();

//            Toast.makeText(context,"ViewLocation",Toast.LENGTH_SHORT).show();
//            Intent intent =new Intent(context,ViewLocation.class);
//            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);


        } else if (id == R.id.nav_historial) {
//            Toast.makeText(context,"Ver el historial",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,OrdenesActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

//            Intent intent =new Intent(context,ViewOrderActivity.class);
//            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);


        } else if (id == R.id.nav_new_order) {
//            Toast.makeText(context,"Nueva Orden",Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(context,NewOrderActivity.class);
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Toast.makeText(context,"Ajustes",Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(context,EmularAuto.class);
//            intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } else if (id == R.id.nav_promo) {
            Toast.makeText(context,"Promociones",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(context,"Enviar",Toast.LENGTH_SHORT).show();

        } else if(id==R.id.nav_login){
//            Toast.makeText(context,"Logout",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Utils.getInstance().clearSharedPreference(context);//.save(this,usermail,"email");

            Intent intent=new Intent(context,LoginActivity.class);//todo: borrar de  los shared prefs
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
}
