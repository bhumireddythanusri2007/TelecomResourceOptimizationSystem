package com.telecom.app;

import com.telecom.dao.OptimizationResultDAO;
import com.telecom.model.OptimizationResult;

import java.util.List;

public class OptimizationResultTest {

    public static void main(String[] args) {

        OptimizationResultDAO dao = new OptimizationResultDAO();

        List<OptimizationResult> results = dao.getAllOptimizationResults();

        System.out.println("Total optimization results: " + results.size());

        for (OptimizationResult result : results) {
            System.out.println(result);
        }
    }
}