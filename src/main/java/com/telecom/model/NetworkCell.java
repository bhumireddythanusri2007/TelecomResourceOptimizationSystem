package com.telecom.model;

public class NetworkCell {

    private int cellId;
    private String cellName;
    private String location;
    private int totalCapacity;
    private int currentUsers;
    private String status;

    public NetworkCell() {
    }

    public NetworkCell(int cellId, String cellName, String location,
                       int totalCapacity, int currentUsers, String status) {

        this.cellId = cellId;
        this.cellName = cellName;
        this.location = location;
        this.totalCapacity = totalCapacity;
        this.currentUsers = currentUsers;
        this.status = status;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public int getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(int currentUsers) {
        this.currentUsers = currentUsers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NetworkCell{" +
                "cellId=" + cellId +
                ", cellName='" + cellName + '\'' +
                ", location='" + location + '\'' +
                ", totalCapacity=" + totalCapacity +
                ", currentUsers=" + currentUsers +
                ", status='" + status + '\'' +
                '}';
    }
}