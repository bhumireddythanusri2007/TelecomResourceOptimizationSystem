package com.telecom.model;

public class AuditLog {

    private int auditId;
    private int userId;
    private String actionType;
    private String actionDescription;
    private String tableName;
    private int recordId;
    private String actionTime;

    public AuditLog() {
    }

    public AuditLog(
            int auditId,
            int userId,
            String actionType,
            String actionDescription,
            String tableName,
            int recordId,
            String actionTime) {

        this.auditId = auditId;
        this.userId = userId;
        this.actionType = actionType;
        this.actionDescription = actionDescription;
        this.tableName = tableName;
        this.recordId = recordId;
        this.actionTime = actionTime;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(
            String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }
}