package com.example.ticketing.controller;

import com.example.ticketing.model.Event;
import com.example.ticketing.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventContoller {

    EventService eventService = new EventService();
    public EventContoller() {
    }

    @CrossOrigin
    @GetMapping("/ticketing/events")
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @CrossOrigin
    @GetMapping("/ticketing/event")
    public ResponseEntity<Event> getProductById(@RequestParam long id) {
        Event event = eventService.getEventById(id);
        return (event == null) ?
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null):
                ResponseEntity.status(HttpStatus.OK).body(event);

    }


}
