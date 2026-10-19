package com.telecom.model;

public class TrafficFlow {

    private int flowId;
    private int cellId;
    private String sourceIp;
    private String destinationIp;
    private String protocol;
    private double dataVolume;
    private double flowDuration;
    private int packetCount;
    private String flowStatus;
    private String recordedTime;

    public TrafficFlow() {
    }

    public TrafficFlow(
            int flowId,
            int cellId,
            String sourceIp,
            String destinationIp,
            String protocol,
            double dataVolume,
            double flowDuration,
            int packetCount,
            String flowStatus,
            String recordedTime) {

        this.flowId = flowId;
        this.cellId = cellId;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.protocol = protocol;
        this.dataVolume = dataVolume;
        this.flowDuration = flowDuration;
        this.packetCount = packetCount;
        this.flowStatus = flowStatus;
        this.recordedTime = recordedTime;
    }

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public double getDataVolume() {
        return dataVolume;
    }

    public void setDataVolume(double dataVolume) {
        this.dataVolume = dataVolume;
    }

    public double getFlowDuration() {
        return flowDuration;
    }

    public void setFlowDuration(double flowDuration) {
        this.flowDuration = flowDuration;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }
}