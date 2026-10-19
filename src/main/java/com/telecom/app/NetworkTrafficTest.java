package com.telecom.app;

import com.telecom.dao.NetworkTrafficDAO;
import com.telecom.model.NetworkTraffic;

import java.util.List;

public class NetworkTrafficTest {

    public static void main(String[] args) {

        NetworkTrafficDAO dao = new NetworkTrafficDAO();

        List<NetworkTraffic> trafficList =
                dao.getAllNetworkTraffic();

        System.out.println(
                "Total network traffic records: " + trafficList.size());

        for (NetworkTraffic traffic : trafficList) {
            System.out.println(traffic);
        }
    }
}