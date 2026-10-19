package com.telecom.model;

public class OptimizationJob {

    private int jobId;
    private String jobName;
    private String optimizationType;
    private int targetCellId;
    private String objective;
    private double inputLoad;
    private String status;
    private String createdTime;

    public OptimizationJob() {
    }

    public OptimizationJob(
            int jobId,
            String jobName,
            String optimizationType,
            int targetCellId,
            String objective,
            double inputLoad,
            String status,
            String createdTime) {

        this.jobId = jobId;
        this.jobName = jobName;
        this.optimizationType = optimizationType;
        this.targetCellId = targetCellId;
        this.objective = objective;
        this.inputLoad = inputLoad;
        this.status = status;
        this.createdTime = createdTime;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getOptimizationType() {
        return optimizationType;
    }

    public void setOptimizationType(String optimizationType) {
        this.optimizationType = optimizationType;
    }

    public int getTargetCellId() {
        return targetCellId;
    }

    public void setTargetCellId(int targetCellId) {
        this.targetCellId = targetCellId;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public double getInputLoad() {
        return inputLoad;
    }

    public void setInputLoad(double inputLoad) {
        this.inputLoad = inputLoad;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}