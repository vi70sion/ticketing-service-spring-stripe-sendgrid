package com.example.ticketing.model;

public class EventReview {
    private long id;
    private long eventId;
    private String userName;
    private int rating;
    private String comment;
    private String createdAt;

    public EventReview() {}

    public EventReview(long eventId, String userName, int rating, String comment) {
        this.eventId = eventId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
    }

    public long getId() { return id; }
    public void setId(long id) {  this.id = id; }

    public long getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }


}

