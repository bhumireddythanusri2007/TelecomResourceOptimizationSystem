package com.telecom.model;

import java.sql.Timestamp;

public class NetworkTraffic {

    private int trafficId;
    private int cellId;
    private double dataUsage;
    private int activeUsers;
    private double trafficLoad;
    private Timestamp recordedTime;

    public NetworkTraffic() {
    }

    public NetworkTraffic(int trafficId, int cellId, double dataUsage,
                          int activeUsers, double trafficLoad,
                          Timestamp recordedTime) {

        this.trafficId = trafficId;
        this.cellId = cellId;
        this.dataUsage = dataUsage;
        this.activeUsers = activeUsers;
        this.trafficLoad = trafficLoad;
        this.recordedTime = recordedTime;
    }

    public int getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(int trafficId) {
        this.trafficId = trafficId;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public double getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(double dataUsage) {
        this.dataUsage = dataUsage;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public double getTrafficLoad() {
        return trafficLoad;
    }

    public void setTrafficLoad(double trafficLoad) {
        this.trafficLoad = trafficLoad;
    }

    public Timestamp getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(Timestamp recordedTime) {
        this.recordedTime = recordedTime;
    }

    @Override
    public String toString() {
        return "NetworkTraffic{" +
                "trafficId=" + trafficId +
                ", cellId=" + cellId +
                ", dataUsage=" + dataUsage +
                ", activeUsers=" + activeUsers +
                ", trafficLoad=" + trafficLoad +
                ", recordedTime=" + recordedTime +
                '}';
    }
}