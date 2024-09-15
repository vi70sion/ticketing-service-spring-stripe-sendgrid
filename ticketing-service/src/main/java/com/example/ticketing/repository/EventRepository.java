package com.example.ticketing.repository;


import com.example.ticketing.model.Event;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.ticketing.Constants.*;

public class EventRepository {
    List<Event> eventList;
    private static Connection _connection;
    public EventRepository() { }

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

    public List<Event> getAllEvents() {
        try {
            Event event;
            sqlConnection();
            eventList = new ArrayList<>();
            String sql = "SELECT * FROM events";
            PreparedStatement statement = _connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                event = new Event();
                event.setId(resultSet.getInt("id"));
                event.setName(resultSet.getString("name"));
                event.setDescription(resultSet.getString("description"));
                event.setEventTime(LocalDateTime.parse(resultSet.getString("event_time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                event.setPrice(resultSet.getBigDecimal("price"));
                event.setImageUrl(resultSet.getString("image_url"));
                eventList.add(event);
            }
            return eventList;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    public Event getEventById(long id) {
        try {
            Event event = new Event();;
            sqlConnection();
            String sql = "SELECT * FROM events WHERE id = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            event.setId(resultSet.getInt("id"));
            event.setName(resultSet.getString("name"));
            event.setDescription(resultSet.getString("description"));
            event.setEventTime(LocalDateTime.parse(resultSet.getString("event_time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            event.setPrice(resultSet.getBigDecimal("price"));
            event.setImageUrl(resultSet.getString("image_url"));
            return event;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return null;
    }

}
