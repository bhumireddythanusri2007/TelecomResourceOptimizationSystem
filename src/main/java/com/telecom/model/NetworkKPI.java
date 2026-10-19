package com.telecom.model;

import java.sql.Timestamp;

public class NetworkKPI {

    private int kpiId;
    private int cellId;
    private double throughput;
    private double latency;
    private double packetLoss;
    private double availability;
    private double signalStrength;
    private Timestamp recordedTime;

    public NetworkKPI() {
    }

    public int getKpiId() {
        return kpiId;
    }

    public void setKpiId(int kpiId) {
        this.kpiId = kpiId;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
    }

    public double getAvailability() {
        return availability;
    }

    public void setAvailability(double availability) {
        this.availability = availability;
    }

    public double getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(double signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Timestamp getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(Timestamp recordedTime) {
        this.recordedTime = recordedTime;
    }

    @Override
    public String toString() {

        return "NetworkKPI{" +
                "kpiId=" + kpiId +
                ", cellId=" + cellId +
                ", throughput=" + throughput +
                ", latency=" + latency +
                ", packetLoss=" + packetLoss +
                ", availability=" + availability +
                ", signalStrength=" + signalStrength +
                ", recordedTime=" + recordedTime +
                '}';
    }
}