package com.telecom.dao;

import com.telecom.model.NetworkKPI;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NetworkKPIDAO {

    public List<NetworkKPI> getAllKPIs() {

        List<NetworkKPI> kpis =
                new ArrayList<>();

        String sql =
                "SELECT * FROM network_kpis " +
                "ORDER BY kpi_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                NetworkKPI kpi =
                        new NetworkKPI();

                kpi.setKpiId(
                        resultSet.getInt("kpi_id"));

                kpi.setCellId(
                        resultSet.getInt("cell_id"));

                kpi.setThroughput(
                        resultSet.getDouble("throughput"));

                kpi.setLatency(
                        resultSet.getDouble("latency"));

                kpi.setPacketLoss(
                        resultSet.getDouble("packet_loss"));

                kpi.setAvailability(
                        resultSet.getDouble("availability"));

                kpi.setSignalStrength(
                        resultSet.getDouble("signal_strength"));

                kpi.setRecordedTime(
                        resultSet.getTimestamp("recorded_time"));

                kpis.add(kpi);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving network KPIs.");

            e.printStackTrace();
        }

        return kpis;
    }
}