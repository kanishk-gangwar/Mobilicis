package com.assigment.mobilicis;

public class DeviceInfoData {
    private String date;
    private String accelerometer;
    private String bluetooth;
    private String gps;
    private String gyroscope;
    private String root;

    private String BackCam;

    private String FrontCam;
    public DeviceInfoData(String date, String accelerometer, String bluetooth, String gps, String gyroscope, String root,String BackCam,String FrontCam) {
        this.date = date;
        this.accelerometer = accelerometer;
        this.bluetooth = bluetooth;
        this.gps = gps;
        this.gyroscope = gyroscope;
        this.root = root;
        this.BackCam = BackCam;
        this.FrontCam = FrontCam;
    }

    public String getBackCam() {
        return BackCam;
    }

    public void setBackCam(String backCam) {
        BackCam = backCam;
    }

    public String getFrontCam() {
        return FrontCam;
    }

    public void setFrontCam(String frontCam) {
        FrontCam = frontCam;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAccelerometer(String accelerometer) {
        this.accelerometer = accelerometer;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public void setGyroscope(String gyroscope) {
        this.gyroscope = gyroscope;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getDate() {
        return date;
    }

    public String getAccelerometer() {
        return accelerometer;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public String getGps() {
        return gps;
    }

    public String getGyroscope() {
        return gyroscope;
    }

    public String getRoot() {
        return root;
    }
}

