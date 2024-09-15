package com.example.ticketing.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.ticketing.Constants.*;
public class EmailRepository {
    private static Connection _connection;
    public EmailRepository() {  }

    public static void sqlConnection() {
        try {
            _connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // connection issues
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // handle any other exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void saveEmailLog(String to, String subject, String status, String errorMessage) {
        try {
            sqlConnection();
            String sql = "INSERT INTO email_logs (email_to, subject, status, error_message) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setString(1, to);
            statement.setString(2, subject);
            statement.setString(3, status);
            if (errorMessage != null) {
                statement.setString(4, errorMessage);
            } else {
                statement.setNull(4, java.sql.Types.VARCHAR);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
    }


}
