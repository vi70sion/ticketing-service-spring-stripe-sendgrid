package com.example.ticketing.repository;

import com.example.ticketing.model.Order;
import static com.example.ticketing.Constants.*;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepository {
    EventRepository eventRepository = new EventRepository();
    private static Connection _connection;
    List<Order> orderList;
    public OrderRepository() { }

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

    public List<Order> getOrdersByUser(String userEmail) {
        try {
            Order order;
            sqlConnection();
            orderList = new ArrayList<>();
            String sql = "SELECT * FROM orders WHERE user_email = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                order = new Order();
                order.setId(resultSet.getLong("id"));
                order.setEventId(resultSet.getLong("event_id"));
                order.setUserEmail(resultSet.getString("user_email"));
                order.setPrice(resultSet.getBigDecimal("price"));
                order.setDiscount(resultSet.getString("discount_name"));
                order.setOrderTotal(resultSet.getBigDecimal("order_total"));
                order.setPaymentStatus(resultSet.getBoolean("payment_status"));
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    public Order getOrderById(long orderId) {
        try {
            Order order;
            sqlConnection();
            String sql = "SELECT * FROM orders WHERE id = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setEventId(resultSet.getLong("event_id"));
            order.setUserEmail(resultSet.getString("user_email"));
            order.setPrice(resultSet.getBigDecimal("price"));
            order.setDiscount(resultSet.getString("discount_name"));
            order.setOrderTotal(resultSet.getBigDecimal("order_total"));
            order.setPaymentStatus(resultSet.getBoolean("payment_status"));
            order.setChargeId(resultSet.getString("charge_id"));
            return order;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return null;
    }

    public BigDecimal addOrder(Order order, UUID uuid) {
        try {
            sqlConnection();
            String sql = "INSERT INTO orders ( event_id, user_email, price, discount_name, order_total, payment_status, uuid) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = _connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setLong(1, order.getEventId());
            statement.setString(2, order.getUserEmail());
            // renginio kaina iš renginių DB
            BigDecimal eventPrice = eventRepository.getEventById(order.getEventId()).getPrice();
            statement.setBigDecimal(3, eventPrice);
            statement.setString(4, order.getDiscount());
            // kaina su nuolaida
            double discPercent = (double)orderDiscount(order.getDiscount());
            BigDecimal totalPrice = eventPrice.multiply(BigDecimal.valueOf(1 - discPercent / 100));
            if (!(eventPrice.compareTo(totalPrice) == 0)) setDiscountCodeAsUsed(order.getDiscount());
            statement.setBigDecimal(5, totalPrice);
            statement.setBoolean(6, false);
            statement.setBytes(7, toBytes(uuid));
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // inserted order ID
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getInt(1);
                    return totalPrice;
                }
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
        return BigDecimal.ZERO;
    }

    public Integer orderDiscount(String discountCode) {
        try {
            int discountValue = 0;
            boolean alreadyUsed = false;
            sqlConnection();
            String sql = "SELECT * FROM discounts WHERE name = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setString(1, discountCode.toUpperCase());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) discountValue = (!resultSet.getBoolean("used")) ? resultSet.getInt("discount"): 0;
            return discountValue;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return 0;
    }

    public void setDiscountCodeAsUsed(String discountCode) {
        try {
            sqlConnection();
            String sql = "UPDATE discounts SET used = ? WHERE name = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setBoolean(1, true);
            statement.setString(2, discountCode.toUpperCase());
            statement.executeUpdate();
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
    }

    public boolean setOrderPaymentStatusTrue(UUID uuid, String chargeId){
        try {
            sqlConnection();
            String sql = "UPDATE orders SET payment_status = ?, charge_id = ? WHERE uuid = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setBoolean(1, true);
            statement.setString(2, chargeId);
            statement.setBytes(3, toBytes(uuid));
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
        return false;
    }

    public boolean markOrderAsRefunded(long orderId){
        try {
            sqlConnection();
            String sql = "UPDATE orders SET payment_status = ? WHERE id = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setBoolean(1, false);
            statement.setLong(2, orderId);
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
        return false;
    }

    public byte[] toBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public UUID fromBytes(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }
}
