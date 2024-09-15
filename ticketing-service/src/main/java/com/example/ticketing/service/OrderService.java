package com.example.ticketing.service;

import com.example.ticketing.repository.OrderRepository;
import com.example.ticketing.model.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    OrderRepository ordRepository = new OrderRepository();

    public OrderService() {  }

    public BigDecimal addOrder(Order order, UUID uuid){
        return ordRepository.addOrder(order, uuid);
    }

    public Integer orderDiscount(String discountCode){
        return ordRepository.orderDiscount(discountCode);
    }

    public boolean setOrderPaymentStatusTrue(UUID uuid, String chargeId){
        return ordRepository.setOrderPaymentStatusTrue(uuid, chargeId);
    }

    public boolean markOrderAsRefunded(long orderId){
        return ordRepository.markOrderAsRefunded(orderId);
    }

    public List<Order> getOrdersByUser(String userEmail) {
        return ordRepository.getOrdersByUser(userEmail);
    }

    public Order getOrderById(long orderId) {
        return ordRepository.getOrderById(orderId);
    }

}
