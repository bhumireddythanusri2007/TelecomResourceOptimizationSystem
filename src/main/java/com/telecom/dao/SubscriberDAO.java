package com.telecom.dao;

import com.telecom.model.Subscriber;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubscriberDAO {

    public List<Subscriber> getAllSubscribers() {

        List<Subscriber> subscribers = new ArrayList<>();

        String sql =
                "SELECT * FROM subscribers " +
                "ORDER BY subscriber_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Subscriber subscriber = new Subscriber();

                subscriber.setSubscriberId(
                        resultSet.getInt("subscriber_id"));

                subscriber.setSubscriberName(
                        resultSet.getString("subscriber_name"));

                subscriber.setPhoneNumber(
                        resultSet.getString("phone_number"));

                subscriber.setEmail(
                        resultSet.getString("email"));

                subscriber.setSubscriptionType(
                        resultSet.getString("subscription_type"));

                subscriber.setDataPlan(
                        resultSet.getString("data_plan"));

                subscriber.setMonthlyDataLimit(
                        resultSet.getDouble("monthly_data_limit"));

                subscriber.setCurrentDataUsage(
                        resultSet.getDouble("current_data_usage"));

                subscriber.setStatus(
                        resultSet.getString("status"));

                subscribers.add(subscriber);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving subscribers.");

            e.printStackTrace();
        }

        return subscribers;
    }

    public boolean addSubscriber(Subscriber subscriber) {

        String sql =
                "INSERT INTO subscribers " +
                "(subscriber_name, phone_number, email, " +
                "subscription_type, data_plan, " +
                "monthly_data_limit, current_data_usage, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, subscriber.getSubscriberName());

            statement.setString(
                    2, subscriber.getPhoneNumber());

            statement.setString(
                    3, subscriber.getEmail());

            statement.setString(
                    4, subscriber.getSubscriptionType());

            statement.setString(
                    5, subscriber.getDataPlan());

            statement.setDouble(
                    6, subscriber.getMonthlyDataLimit());

            statement.setDouble(
                    7, subscriber.getCurrentDataUsage());

            statement.setString(
                    8, subscriber.getStatus());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding subscriber.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateSubscriber(Subscriber subscriber) {

        String sql =
                "UPDATE subscribers SET " +
                "subscriber_name = ?, " +
                "phone_number = ?, " +
                "email = ?, " +
                "subscription_type = ?, " +
                "data_plan = ?, " +
                "monthly_data_limit = ?, " +
                "current_data_usage = ?, " +
                "status = ? " +
                "WHERE subscriber_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, subscriber.getSubscriberName());

            statement.setString(
                    2, subscriber.getPhoneNumber());

            statement.setString(
                    3, subscriber.getEmail());

            statement.setString(
                    4, subscriber.getSubscriptionType());

            statement.setString(
                    5, subscriber.getDataPlan());

            statement.setDouble(
                    6, subscriber.getMonthlyDataLimit());

            statement.setDouble(
                    7, subscriber.getCurrentDataUsage());

            statement.setString(
                    8, subscriber.getStatus());

            statement.setInt(
                    9, subscriber.getSubscriberId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating subscriber.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteSubscriber(int subscriberId) {

        String sql =
                "DELETE FROM subscribers " +
                "WHERE subscriber_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, subscriberId);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting subscriber.");

            e.printStackTrace();

            return false;
        }
    }
}