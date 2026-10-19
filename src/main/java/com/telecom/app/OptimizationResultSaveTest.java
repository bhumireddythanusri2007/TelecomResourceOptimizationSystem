package com.telecom.app;

import com.telecom.dao.OptimizationResultDAO;
import com.telecom.model.OptimizationResult;

public class OptimizationResultSaveTest {

    public static void main(String[] args) {

        OptimizationResult result = new OptimizationResult();

        result.setCellId(1);
        result.setPreviousLoad(75.0);
        result.setOptimizedLoad(65.0);
        result.setAllocatedBandwidth(110);
        result.setPerformanceScore(86.5);

        OptimizationResultDAO dao = new OptimizationResultDAO();

        boolean saved = dao.saveOptimizationResult(result);

        if (saved) {
            System.out.println("Optimization result saved successfully!");
        } else {
            System.out.println("Failed to save optimization result.");
        }
    }
}