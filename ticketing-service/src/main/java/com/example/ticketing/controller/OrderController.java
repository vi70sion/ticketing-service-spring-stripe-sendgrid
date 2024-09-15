package com.example.ticketing.controller;

import com.example.ticketing.model.Order;
import com.example.ticketing.service.OrderService;
import com.example.ticketing.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {

    OrderService orderService = new OrderService();

    @CrossOrigin
    @GetMapping("/ticketing/discount")
    public ResponseEntity<Integer> orderAmount(@RequestParam String discountCode) {
        return ResponseEntity.ok(orderService.orderDiscount(discountCode));
    }

    @CrossOrigin
    @GetMapping("/ticketing/orders")
    public List<Order> getOrdersByUser(@RequestParam String userEmail) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userEmail)).getBody();
    }

}
