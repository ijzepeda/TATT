package com.ijzepeda.tatt;

/**
 * Created by Ivan on 9/22/2016.
 */

public class Order {

    String orderNo;
    String orderName;
//    String nameUser;
    String mail;
    String uid;

    String originStreet;
    double originLatitude;
    double originLongitude;

    String destinationStreet;
    double destinationLatitude;
    double destinationLongitude;

    String vehicle;
    String cargoType;
    String flowType;
    String cost;
    String distance;
    String eta;
    String finalTime;
    String status;
    String paymentType;
    String paymentId;
    String vehicleId;

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNo='" + orderNo + '\'' +
                ", orderName='" + orderName + '\'' +
                ", mail='" + mail + '\'' +
                ", uid='" + uid + '\'' +
                ", originStreet='" + originStreet + '\'' +
                ", originLatitude=" + originLatitude +
                ", originLongitude=" + originLongitude +
                ", destinationStreet='" + destinationStreet + '\'' +
                ", destinationLatitude=" + destinationLatitude +
                ", destinationLongitude=" + destinationLongitude +
                ", vehicle='" + vehicle + '\'' +
                ", cargoType='" + cargoType + '\'' +
                ", flowType='" + flowType + '\'' +
                ", cost='" + cost + '\'' +
                ", distance='" + distance + '\'' +
                ", eta='" + eta + '\'' +
                ", finalTime='" + finalTime + '\'' +
                ", status='" + status + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", vehicleId='" + vehicleId+ '\'' +
                '}';
    }

    public Order(String orderNo, String orderName, String mail, String uid, String originStreet, double originLatitude, double originLongitude, String destinationStreet, double destinationLatitude, double destinationLongitude, String vehicle, String cargoType, String flowType, String cost, String distance, String eta, String finalTime, String status, String paymentType, String paymentId, String vehicleId) {
        this.orderNo = orderNo;
        this.orderName = orderName;
        this.mail = mail;
        this.uid = uid;
        this.originStreet = originStreet;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destinationStreet = destinationStreet;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.vehicle = vehicle;
        this.cargoType = cargoType;
        this.flowType = flowType;
        this.cost = cost;
        this.distance = distance;
        this.eta = eta;
        this.finalTime = finalTime;
        this.status = status;
        this.paymentType = paymentType;
        this.paymentId = paymentId;
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOriginStreet() {
        return originStreet;
    }

    public void setOriginStreet(String originStreet) {
        this.originStreet = originStreet;
    }

    public double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public String getDestinationStreet() {
        return destinationStreet;
    }

    public void setDestinationStreet(String destinationStreet) {
        this.destinationStreet = destinationStreet;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


}
