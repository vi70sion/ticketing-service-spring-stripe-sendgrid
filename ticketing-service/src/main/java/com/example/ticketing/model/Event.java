package com.example.ticketing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Event {
    private long id;
    private String name;
    private String description;
    private LocalDateTime eventTime;
    private BigDecimal price;
    private String imageUrl;

    public Event() { }

    public Event(long id, String name, String description, LocalDateTime eventTime, BigDecimal price, String photoUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventTime = eventTime;
        this.price = price;
        this.imageUrl = photoUrl;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getEventTime() { return eventTime; }
    public BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }


}
