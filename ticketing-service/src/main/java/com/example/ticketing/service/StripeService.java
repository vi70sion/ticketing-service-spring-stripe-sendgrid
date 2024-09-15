package com.example.ticketing.service;

import com.example.ticketing.model.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class StripeService {

    OrderService orderService = new OrderService();

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
                        .build();

        return PaymentIntent.create(params);
    }

    public Session createCheckoutSession(Order order) throws StripeException {
        UUID uuid = UUID.randomUUID();
        BigDecimal amount = orderService.addOrder(order, uuid);
        if(amount.equals(BigDecimal.ZERO))
            return new Session();
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/ticketing/redirect?uuid=" + uuid + "&sessionId={CHECKOUT_SESSION_ID}")
                //.setCancelUrl("http://localhost:7777/index.html")
                .setCancelUrl("http://127.0.0.1:5500/faild.html")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("ticketing order")
                                        .build())
                                .build())
                        .build())
                .build();

        return Session.create(params);
    }

//    paprastesnis, bet nenaudoju, nes neranda getCharges() metodo
//    public String getChargeIdFromSession(String sessionId) throws StripeException {
//        Session session = Session.retrieve(sessionId);
//        String paymentIntentId = session.getPaymentIntent(); // Get paymentIntentId from session
//        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
//
//        // Return the first charge associated with the payment intent
//        return paymentIntent.getCharges().getData().get(0).getId();
//    }

    public String getChargeIdFromSession(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        String paymentIntentId = session.getPaymentIntent();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        // Instead of using getCharges(), you can loop through the PaymentIntent's associated charges
        // using the event data from the PaymentIntent's last payment error or outcome.
        // Retrieve charges using PaymentIntent
        ChargeCollection chargeCollection = Charge.list(
                Map.of("payment_intent", paymentIntentId)
        );
        // Check if charges exist and return the first charge ID
        if (chargeCollection != null && !chargeCollection.getData().isEmpty()) {
            return chargeCollection.getData().get(0).getId();
        } else {
            return null;
        }
    }


}
