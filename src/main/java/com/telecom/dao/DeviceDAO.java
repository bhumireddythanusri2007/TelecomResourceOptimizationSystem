package com.telecom.dao;

import com.telecom.model.Device;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    public List<Device> getAllDevices() {

        List<Device> devices =
                new ArrayList<>();

        String sql =
                "SELECT * FROM devices " +
                "ORDER BY device_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Device device =
                        new Device();

                device.setDeviceId(
                        resultSet.getInt("device_id"));

                device.setUserId(
                        resultSet.getInt("user_id"));

                device.setDeviceName(
                        resultSet.getString("device_name"));

                device.setDeviceType(
                        resultSet.getString("device_type"));

                device.setIpAddress(
                        resultSet.getString("ip_address"));

                device.setConnectionType(
                        resultSet.getString("connection_type"));

                device.setStatus(
                        resultSet.getString("status"));

                devices.add(device);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving devices.");

            e.printStackTrace();
        }

        return devices;
    }

    public boolean addDevice(Device device) {

        String sql =
                "INSERT INTO devices " +
                "(user_id, device_name, device_type, " +
                "ip_address, connection_type, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    device.getUserId());

            statement.setString(
                    2,
                    device.getDeviceName());

            statement.setString(
                    3,
                    device.getDeviceType());

            statement.setString(
                    4,
                    device.getIpAddress());

            statement.setString(
                    5,
                    device.getConnectionType());

            statement.setString(
                    6,
                    device.getStatus());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding device.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateDevice(Device device) {

        String sql =
                "UPDATE devices SET " +
                "user_id = ?, " +
                "device_name = ?, " +
                "device_type = ?, " +
                "ip_address = ?, " +
                "connection_type = ?, " +
                "status = ? " +
                "WHERE device_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    device.getUserId());

            statement.setString(
                    2,
                    device.getDeviceName());

            statement.setString(
                    3,
                    device.getDeviceType());

            statement.setString(
                    4,
                    device.getIpAddress());

            statement.setString(
                    5,
                    device.getConnectionType());

            statement.setString(
                    6,
                    device.getStatus());

            statement.setInt(
                    7,
                    device.getDeviceId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating device.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteDevice(int deviceId) {

        String sql =
                "DELETE FROM devices " +
                "WHERE device_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    deviceId);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting device.");

            e.printStackTrace();

            return false;
        }
    }
}