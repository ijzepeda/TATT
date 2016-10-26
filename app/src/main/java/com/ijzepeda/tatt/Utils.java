package com.ijzepeda.tatt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.google.android.gms.analytics.internal.zzy.u;

/**
 * Created by Ivan on 9/28/2016.
 */

public class Utils {
    private String userMail;
    private String uid;
    private String userName;
//    private LatLng currentLocation;
//    private static SharedPreferences sharedPrefs;
    private static Utils UtilSharedPrefs;
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";



    public static Utils getInstance()
    {
        if (UtilSharedPrefs == null)
        {
            UtilSharedPrefs = new Utils();
        }
        return UtilSharedPrefs;
    }

    public Utils() {
        super();
    }

    public void save(Context context, String text , String Key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(Key, text); //3

        editor.commit(); //4


    }

    public String getValue(Context context , String Key) {
        SharedPreferences settings;
        String text = "";
        //  settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(Key, "");
        return text;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context , String value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(value);
        editor.commit();
    }




    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
