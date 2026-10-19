package com.telecom.model;

public class ResourceAllocation {

    private int allocationId;
    private int cellId;
    private int bandwidth;
    private int frequency;
    private int allocatedUsers;

    public ResourceAllocation() {
    }

    public ResourceAllocation(int allocationId, int cellId, int bandwidth,
                              int frequency, int allocatedUsers) {

        this.allocationId = allocationId;
        this.cellId = cellId;
        this.bandwidth = bandwidth;
        this.frequency = frequency;
        this.allocatedUsers = allocatedUsers;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getAllocatedUsers() {
        return allocatedUsers;
    }

    public void setAllocatedUsers(int allocatedUsers) {
        this.allocatedUsers = allocatedUsers;
    }

    @Override
    public String toString() {
        return "ResourceAllocation{" +
                "allocationId=" + allocationId +
                ", cellId=" + cellId +
                ", bandwidth=" + bandwidth +
                ", frequency=" + frequency +
                ", allocatedUsers=" + allocatedUsers +
                '}';
    }
}