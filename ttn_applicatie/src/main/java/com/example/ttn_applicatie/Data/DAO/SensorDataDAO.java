package com.example.ttn_applicatie.Data.DAO;

import java.time.LocalDateTime;
import java.util.Date;

public class SensorDataDAO {
    private String id;
    private LocalDateTime receivedAt;
    private String deviceId;
    private double temperature;
    private double phValue;

    public SensorDataDAO(String id, LocalDateTime receivedAt, String deviceId, double temperature, double phValue){
        this.id = id;
        this.receivedAt = receivedAt;
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.phValue = phValue;
    }

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

    @Override
    public String toString() {
        return id + "[" + receivedAt + ", " + deviceId + ", [" + temperature + " . " + phValue + "]]";
    }
}
