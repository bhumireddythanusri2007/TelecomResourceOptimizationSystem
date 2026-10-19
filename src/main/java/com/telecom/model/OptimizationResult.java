package com.telecom.model;

import java.sql.Timestamp;

public class OptimizationResult {

    private int resultId;
    private int cellId;
    private double previousLoad;
    private double optimizedLoad;
    private int allocatedBandwidth;
    private double performanceScore;
    private Timestamp optimizationTime;

    public OptimizationResult() {
    }

    public OptimizationResult(int resultId, int cellId, double previousLoad,
                              double optimizedLoad, int allocatedBandwidth,
                              double performanceScore,
                              Timestamp optimizationTime) {

        this.resultId = resultId;
        this.cellId = cellId;
        this.previousLoad = previousLoad;
        this.optimizedLoad = optimizedLoad;
        this.allocatedBandwidth = allocatedBandwidth;
        this.performanceScore = performanceScore;
        this.optimizationTime = optimizationTime;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public double getPreviousLoad() {
        return previousLoad;
    }

    public void setPreviousLoad(double previousLoad) {
        this.previousLoad = previousLoad;
    }

    public double getOptimizedLoad() {
        return optimizedLoad;
    }

    public void setOptimizedLoad(double optimizedLoad) {
        this.optimizedLoad = optimizedLoad;
    }

    public int getAllocatedBandwidth() {
        return allocatedBandwidth;
    }

    public void setAllocatedBandwidth(int allocatedBandwidth) {
        this.allocatedBandwidth = allocatedBandwidth;
    }

    public double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public Timestamp getOptimizationTime() {
        return optimizationTime;
    }

    public void setOptimizationTime(Timestamp optimizationTime) {
        this.optimizationTime = optimizationTime;
    }

    @Override
    public String toString() {
        return "OptimizationResult{" +
                "resultId=" + resultId +
                ", cellId=" + cellId +
                ", previousLoad=" + previousLoad +
                ", optimizedLoad=" + optimizedLoad +
                ", allocatedBandwidth=" + allocatedBandwidth +
                ", performanceScore=" + performanceScore +
                ", optimizationTime=" + optimizationTime +
                '}';
    }
}