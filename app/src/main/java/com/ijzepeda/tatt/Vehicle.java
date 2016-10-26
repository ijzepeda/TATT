package com.ijzepeda.tatt;

/**
 * Created by Ivan on 10/5/2016.
 */

public class Vehicle {
//    active:            true
//    car:            "camioneta--1"
//    latitude:            19.3205464
//    longitude:            -99.624662
//    vehicleId:            "camioneta123"
    boolean active;
    String car;
    double latitude;
    double longitude;
    String vehicleId;

    public Vehicle() {
    }

    public Vehicle(boolean active, String car, double latitude, double longitude, String vehicleId) {
        this.active = active;
        this.car = car;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicleId = vehicleId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
