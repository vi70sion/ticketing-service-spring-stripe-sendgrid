package com.example.ticketing.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static com.example.ticketing.Constants.*;

public class NewslettersRepository {
    private static Connection _connection;
    public NewslettersRepository() {  }
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

    public boolean subscribe(String userEmail) {
        try {
            sqlConnection();
            String sql = "INSERT INTO newsletters_users (email) VALUES (?)";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setString(1, userEmail);
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return false;
    }


}
