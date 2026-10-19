package com.telecom.app;

import com.telecom.service.ResourceOptimizationService;

public class ResourceOptimizationTest {

    public static void main(String[] args) {

        ResourceOptimizationService service =
                new ResourceOptimizationService();

        service.performOptimization();
    }
}