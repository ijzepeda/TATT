package com.ijzepeda.tatt;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ivan on 9/22/2016.
 */

public class PinLocation {

    //        implements Parcelable
    LatLng latLng;
    String address;

    public PinLocation(LatLng latLng, String address) {
        this.latLng = latLng;
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel parcel, int i) {
//            parcel. latLng;
//             address;
//        }
}
