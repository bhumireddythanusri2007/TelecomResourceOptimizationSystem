package com.telecom.app;

import com.telecom.dao.ResourceAllocationDAO;
import com.telecom.model.ResourceAllocation;

import java.util.List;

public class ResourceAllocationTest {

    public static void main(String[] args) {

        ResourceAllocationDAO dao = new ResourceAllocationDAO();

        List<ResourceAllocation> allocations =
                dao.getAllResourceAllocations();

        System.out.println(
                "Total resource allocations: " + allocations.size());

        for (ResourceAllocation allocation : allocations) {
            System.out.println(allocation);
        }
    }
}