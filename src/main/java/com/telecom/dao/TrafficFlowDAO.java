package com.telecom.dao;

import com.telecom.model.TrafficFlow;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TrafficFlowDAO {

    public List<TrafficFlow> getAllTrafficFlows() {

        List<TrafficFlow> flows = new ArrayList<>();

        String sql =
                "SELECT * FROM traffic_flows " +
                "ORDER BY flow_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                TrafficFlow flow =
                        new TrafficFlow();

                flow.setFlowId(
                        resultSet.getInt("flow_id"));

                flow.setCellId(
                        resultSet.getInt("cell_id"));

                flow.setSourceIp(
                        resultSet.getString("source_ip"));

                flow.setDestinationIp(
                        resultSet.getString(
                                "destination_ip"));

                flow.setProtocol(
                        resultSet.getString("protocol"));

                flow.setDataVolume(
                        resultSet.getDouble("data_volume"));

                flow.setFlowDuration(
                        resultSet.getDouble(
                                "flow_duration"));

                flow.setPacketCount(
                        resultSet.getInt("packet_count"));

                flow.setFlowStatus(
                        resultSet.getString("flow_status"));

                flow.setRecordedTime(
                        resultSet.getTimestamp(
                                "recorded_time")
                                .toString());

                flows.add(flow);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving traffic flows.");

            e.printStackTrace();
        }

        return flows;
    }

    public boolean addTrafficFlow(
            TrafficFlow flow) {

        String sql =
                "INSERT INTO traffic_flows " +
                "(cell_id, source_ip, destination_ip, " +
                "protocol, data_volume, flow_duration, " +
                "packet_count, flow_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, flow.getCellId());

            statement.setString(
                    2, flow.getSourceIp());

            statement.setString(
                    3, flow.getDestinationIp());

            statement.setString(
                    4, flow.getProtocol());

            statement.setDouble(
                    5, flow.getDataVolume());

            statement.setDouble(
                    6, flow.getFlowDuration());

            statement.setInt(
                    7, flow.getPacketCount());

            statement.setString(
                    8, flow.getFlowStatus());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding traffic flow.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateTrafficFlow(
            TrafficFlow flow) {

        String sql =
                "UPDATE traffic_flows SET " +
                "cell_id = ?, " +
                "source_ip = ?, " +
                "destination_ip = ?, " +
                "protocol = ?, " +
                "data_volume = ?, " +
                "flow_duration = ?, " +
                "packet_count = ?, " +
                "flow_status = ? " +
                "WHERE flow_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, flow.getCellId());

            statement.setString(
                    2, flow.getSourceIp());

            statement.setString(
                    3, flow.getDestinationIp());

            statement.setString(
                    4, flow.getProtocol());

            statement.setDouble(
                    5, flow.getDataVolume());

            statement.setDouble(
                    6, flow.getFlowDuration());

            statement.setInt(
                    7, flow.getPacketCount());

            statement.setString(
                    8, flow.getFlowStatus());

            statement.setInt(
                    9, flow.getFlowId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating traffic flow.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteTrafficFlow(
            int flowId) {

        String sql =
                "DELETE FROM traffic_flows " +
                "WHERE flow_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, flowId);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting traffic flow.");

            e.printStackTrace();

            return false;
        }
    }
}