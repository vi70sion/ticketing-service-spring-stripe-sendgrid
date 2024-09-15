package com.example.ticketing.service;

import com.example.ticketing.model.Event;
import com.example.ticketing.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    EventRepository eventRepository = new EventRepository();
    public EventService() { }

    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    public Event getEventById(long id) {
        return eventRepository.getEventById(id);
    }
}
