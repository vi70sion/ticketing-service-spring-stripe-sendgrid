package com.example.ticketing.model;

import java.math.BigDecimal;

public class Order {
    private long id;
    private long eventId;
    private String userEmail;
    private BigDecimal price;
    private String discount;
    private BigDecimal orderTotal;
    private String chargeId;
    private boolean paymentStatus;

    public Order() {  }
    public Order(long id, long eventId, String userEmail, BigDecimal price, String discount, BigDecimal orderTotal, boolean paymentStatus, String chargeId) {
        this.id = id;
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.price = price;
        this.discount = discount;
        this.orderTotal = orderTotal;
        this.paymentStatus = paymentStatus;
        this.chargeId = chargeId;
    }

    public long getId() { return id; }
    public long getEventId() { return eventId; }
    public String getUserEmail() { return userEmail; }
    public BigDecimal getPrice() { return price; }
    public String getDiscount() { return discount; }
    public BigDecimal getOrderTotal() { return orderTotal; }
    public boolean getPaymentStatus() { return paymentStatus;  }
    public String getChargeId() { return chargeId; }

    public void setId(long id) { this.id = id; }
    public void setEventId(long eventId) { this.eventId = eventId; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setDiscount(String discount) { this.discount = discount; }
    public void setOrderTotal(BigDecimal orderTotal) { this.orderTotal = orderTotal; }
    public void setPaymentStatus(boolean paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setChargeId(String chargeId) { this.chargeId = chargeId; }

}
