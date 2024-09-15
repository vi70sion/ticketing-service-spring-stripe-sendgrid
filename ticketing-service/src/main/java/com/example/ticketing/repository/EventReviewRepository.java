package com.example.ticketing.repository;

import com.example.ticketing.model.EventReview;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.example.ticketing.Constants.*;

@Repository
public class EventReviewRepository {

    private static Connection _connection;
    public EventReviewRepository() { }

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


    public void saveEventReview(EventReview review) {
        try {
            sqlConnection();
            String sql = "INSERT INTO event_reviews (event_id, user_name, rating, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setLong(1, review.getEventId());
            statement.setString(2, review.getUserName());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EventReview> getReviewsByEventId(long eventId) {
        List<EventReview> reviews = new ArrayList<>();
        try {
            sqlConnection();
            String sql = "SELECT * FROM event_reviews WHERE event_id = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setLong(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                EventReview review = new EventReview();
                review.setId(resultSet.getLong("id"));
                review.setEventId(resultSet.getInt("event_id"));
                review.setUserName(resultSet.getString("user_name"));
                review.setRating(resultSet.getInt("rating"));
                review.setComment(resultSet.getString("comment"));
                review.setCreatedAt(resultSet.getString("created_at"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }


}

