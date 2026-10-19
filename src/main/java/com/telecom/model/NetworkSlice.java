package com.telecom.model;

public class NetworkSlice {

    private int sliceId;
    private String sliceName;
    private String sliceType;
    private int bandwidth;
    private double latencyRequirement;
    private int connectedUsers;
    private String status;

    public NetworkSlice() {
    }

    public NetworkSlice(
            int sliceId,
            String sliceName,
            String sliceType,
            int bandwidth,
            double latencyRequirement,
            int connectedUsers,
            String status) {

        this.sliceId = sliceId;
        this.sliceName = sliceName;
        this.sliceType = sliceType;
        this.bandwidth = bandwidth;
        this.latencyRequirement = latencyRequirement;
        this.connectedUsers = connectedUsers;
        this.status = status;
    }

    public int getSliceId() {
        return sliceId;
    }

    public void setSliceId(int sliceId) {
        this.sliceId = sliceId;
    }

    public String getSliceName() {
        return sliceName;
    }

    public void setSliceName(String sliceName) {
        this.sliceName = sliceName;
    }

    public String getSliceType() {
        return sliceType;
    }

    public void setSliceType(String sliceType) {
        this.sliceType = sliceType;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public double getLatencyRequirement() {
        return latencyRequirement;
    }

    public void setLatencyRequirement(
            double latencyRequirement) {

        this.latencyRequirement =
                latencyRequirement;
    }

    public int getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(
            int connectedUsers) {

        this.connectedUsers =
                connectedUsers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}