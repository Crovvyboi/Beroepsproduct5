package com.example.ttn_applicatie.Business.Models;

import com.example.ttn_applicatie.Data.DAO.SensorDataDAO;

import java.time.LocalDateTime;
import java.util.Date;

public class SensorDataObj {
    private String id;
    private LocalDateTime receivedAt;
    private String deviceId;
    private double temperature;
    private double phValue;

    public SensorDataObj(String id, LocalDateTime receivedAt, String deviceId, double temperature, double phValue){
        this.id = id;
        this.receivedAt = receivedAt;
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.phValue = phValue;
    }

    public SensorDataObj(SensorDataDAO dao){
        this.id = dao.getId();
        this.receivedAt = dao.getReceivedAt();
        this.deviceId = dao.getDeviceId();
        this.temperature = dao.getTemperature();
        this.phValue = dao.getPhValue();
    }

    public boolean CompareTempToAcceptable(){
        if (temperature < 25 && temperature > 10){
            return true;
        }
        return false;
    }

    public boolean ComparePHToAcceptable(){
        if (phValue < 6.5 && phValue > 9){
            return true;
        }
        return false;
    }

    // Getters + Setters ---------------------------------------------
    public String getId() {
        return id;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPhValue() {
        return phValue;
    }
}
