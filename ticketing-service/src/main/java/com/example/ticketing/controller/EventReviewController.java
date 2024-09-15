package com.example.ticketing.controller;

import com.example.ticketing.model.EventReview;
import com.example.ticketing.service.EventReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventReviewController {

    @Autowired
    private EventReviewService eventReviewService;

    @PostMapping("/ticketing/event-review-submit")
    public String submitReview(@RequestParam long eventId,
                               @RequestParam String userName,
                               @RequestParam int rating,
                               @RequestParam String comment) {
        eventReviewService.submitReview(eventId, userName, rating, comment);
        return "Review submitted successfully!";
    }

    @GetMapping("/{eventId}")
    public List<EventReview> getReviews(@PathVariable int eventId) {
        return eventReviewService.getReviewsForEvent(eventId);
    }


}

