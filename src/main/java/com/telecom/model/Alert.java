package com.telecom.model;

import java.sql.Timestamp;

public class Alert {

    private int alertId;
    private int cellId;
    private String alertType;
    private String alertMessage;
    private String severity;
    private String status;
    private Timestamp createdTime;

    public Alert() {
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {

        return "Alert{" +
                "alertId=" + alertId +
                ", cellId=" + cellId +
                ", alertType='" + alertType + '\'' +
                ", alertMessage='" + alertMessage + '\'' +
                ", severity='" + severity + '\'' +
                ", status='" + status + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}