package com.telecom.dao;

import com.telecom.model.AuditLog;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

    public List<AuditLog> getAllAuditLogs() {

        List<AuditLog> logs =
                new ArrayList<>();

        String sql =
                "SELECT * FROM audit_log " +
                "ORDER BY audit_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                AuditLog log =
                        new AuditLog();

                log.setAuditId(
                        resultSet.getInt("audit_id"));

                log.setUserId(
                        resultSet.getInt("user_id"));

                log.setActionType(
                        resultSet.getString(
                                "action_type"));

                log.setActionDescription(
                        resultSet.getString(
                                "action_description"));

                log.setTableName(
                        resultSet.getString(
                                "table_name"));

                log.setRecordId(
                        resultSet.getInt(
                                "record_id"));

                log.setActionTime(
                        resultSet.getTimestamp(
                                "action_time")
                                .toString());

                logs.add(log);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving audit logs.");

            e.printStackTrace();
        }

        return logs;
    }

    public boolean addAuditLog(
            AuditLog log) {

        String sql =
                "INSERT INTO audit_log " +
                "(user_id, action_type, " +
                "action_description, table_name, " +
                "record_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, log.getUserId());

            statement.setString(
                    2, log.getActionType());

            statement.setString(
                    3, log.getActionDescription());

            statement.setString(
                    4, log.getTableName());

            statement.setInt(
                    5, log.getRecordId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding audit log.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateAuditLog(
            AuditLog log) {

        String sql =
                "UPDATE audit_log SET " +
                "user_id = ?, " +
                "action_type = ?, " +
                "action_description = ?, " +
                "table_name = ?, " +
                "record_id = ? " +
                "WHERE audit_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, log.getUserId());

            statement.setString(
                    2, log.getActionType());

            statement.setString(
                    3, log.getActionDescription());

            statement.setString(
                    4, log.getTableName());

            statement.setInt(
                    5, log.getRecordId());

            statement.setInt(
                    6, log.getAuditId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating audit log.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteAuditLog(
            int auditId) {

        String sql =
                "DELETE FROM audit_log " +
                "WHERE audit_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, auditId);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting audit log.");

            e.printStackTrace();

            return false;
        }
    }
}