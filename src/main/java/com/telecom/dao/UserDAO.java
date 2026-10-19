package com.telecom.dao;

import com.telecom.model.User;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql =
                "SELECT * FROM users ORDER BY user_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                User user = new User();

                user.setUserId(
                        resultSet.getInt("user_id"));

                user.setUsername(
                        resultSet.getString("username"));

                user.setEmail(
                        resultSet.getString("email"));

                user.setPassword(
                        resultSet.getString("password"));

                user.setRole(
                        resultSet.getString("role"));

                user.setStatus(
                        resultSet.getString("status"));

                user.setCreatedAt(
                        resultSet.getTimestamp("created_at"));

                users.add(user);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving users.");

            e.printStackTrace();
        }

        return users;
    }


    public boolean addUser(User user) {

        String sql =
                "INSERT INTO users " +
                "(username, email, password, role, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, user.getUsername());

            statement.setString(
                    2, user.getEmail());

            statement.setString(
                    3, user.getPassword());

            statement.setString(
                    4, user.getRole());

            statement.setString(
                    5, user.getStatus());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error adding user.");

            e.printStackTrace();

            return false;
        }
    }


    public boolean updateUser(User user) {

        String sql =
                "UPDATE users SET " +
                "username = ?, " +
                "email = ?, " +
                "password = ?, " +
                "role = ?, " +
                "status = ? " +
                "WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, user.getUsername());

            statement.setString(
                    2, user.getEmail());

            statement.setString(
                    3, user.getPassword());

            statement.setString(
                    4, user.getRole());

            statement.setString(
                    5, user.getStatus());

            statement.setInt(
                    6, user.getUserId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error updating user.");

            e.printStackTrace();

            return false;
        }
    }


    public boolean deleteUser(int userId) {

        String sql =
                "DELETE FROM users " +
                "WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, userId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting user.");

            e.printStackTrace();

            return false;
        }
    }


    public User loginUser(
            String email,
            String password) {

        String sql =
                "SELECT * FROM users " +
                "WHERE email = ? " +
                "AND password = ? " +
                "AND status = 'Active'";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, email);

            statement.setString(
                    2, password);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    User user = new User();

                    user.setUserId(
                            resultSet.getInt("user_id"));

                    user.setUsername(
                            resultSet.getString("username"));

                    user.setEmail(
                            resultSet.getString("email"));

                    user.setPassword(
                            resultSet.getString("password"));

                    user.setRole(
                            resultSet.getString("role"));

                    user.setStatus(
                            resultSet.getString("status"));

                    System.out.println(
                            "Login user found: "
                            + email);

                    return user;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error during user login.");

            e.printStackTrace();
        }

        System.out.println(
                "Login failed: no matching active user found."
        );

        return null;
    }
}