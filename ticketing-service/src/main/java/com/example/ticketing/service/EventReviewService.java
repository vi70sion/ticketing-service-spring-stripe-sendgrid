package com.example.ticketing.service;

import com.example.ticketing.model.EventReview;
import com.example.ticketing.repository.EventReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventReviewService {

    @Autowired
    private EventReviewRepository eventReviewRepository;

    public void submitReview(long eventId, String userName, int rating, String comment) {
        EventReview review = new EventReview(eventId, userName, rating, comment);
        eventReviewRepository.saveEventReview(review);
    }

    public List<EventReview> getReviewsForEvent(long eventId) {
        return eventReviewRepository.getReviewsByEventId(eventId);
    }
}

