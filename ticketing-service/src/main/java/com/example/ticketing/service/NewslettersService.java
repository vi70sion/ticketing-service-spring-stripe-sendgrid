package com.example.ticketing.service;

import com.example.ticketing.model.Order;
import com.example.ticketing.repository.NewslettersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.util.UUID;

@Service
public class NewslettersService {

    NewslettersRepository newslettersRepository = new NewslettersRepository();
    public boolean subscribe(String userEmail){
        return newslettersRepository.subscribe(userEmail);
    }
}
