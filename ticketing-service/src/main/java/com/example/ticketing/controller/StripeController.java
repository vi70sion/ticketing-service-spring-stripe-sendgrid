package com.example.ticketing.controller;

import com.example.ticketing.model.Order;
import com.example.ticketing.service.OrderService;
import com.example.ticketing.service.StripeService;
import com.example.ticketing.service.TokenService;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class StripeController {

    private final EmailController emailController;
    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderService orderService;

    public StripeController(EmailController emailController) {
        this.emailController = emailController;
    }

    @PostMapping("/ticketing/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Order order) {
        try {
            Session session = stripeService.createCheckoutSession(order);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

//    naudotas be sessionId, veikiantis
//    @GetMapping("/ticketing/redirect")
//    public RedirectView redirect(@RequestParam UUID uuid){
//        return (orderService.setOrderPaymentStatusTrue(uuid)) ?
//                new RedirectView("http://127.0.0.1:5500/success.html"): // status update successful
//                new RedirectView("http://127.0.0.1:5500/faild.html");
//    }


    @GetMapping("/ticketing/redirect")
    public RedirectView redirect(@RequestParam UUID uuid, @RequestParam String sessionId) {
        try {
            String chargeId = stripeService.getChargeIdFromSession(sessionId);
            boolean isPaymentSuccess = orderService.setOrderPaymentStatusTrue(uuid, chargeId);

            return isPaymentSuccess
                    ? new RedirectView("http://127.0.0.1:5500/success.html")
                    : new RedirectView("http://127.0.0.1:5500/faild.html");

        } catch (StripeException e) {
            // Handle error while retrieving chargeId
            e.printStackTrace();
            return new RedirectView("http://127.0.0.1:5500/faild.html");
        }
    }

    @PostMapping("/ticketing/refund")
    public ResponseEntity<String> refundOrder(@RequestParam Long orderId, @RequestHeader("Authorization") String authorizationHeader) {
        if(!TokenService.handleAuthorization(authorizationHeader).getBody().equals("authorized"))
            return new ResponseEntity<>("Autorizacijos klaida.", HttpStatus.UNAUTHORIZED);

        // norimo grąžinti orderio informacijos tikrinimas
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getPaymentStatus()) {
            return new ResponseEntity<>("Klaidingas orderio numeris arba dar neapmokėtas", HttpStatus.BAD_REQUEST);
        }

        // Create the Stripe refund using Stripe's API
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("charge", order.getChargeId());  // Stripe charge ID
            Refund refund = Refund.create(params);

            if(orderService.markOrderAsRefunded(orderId)) {
                // siūsti grąžinimo patvirtinimą email
                EmailController.EmailRequest emailRequest = new EmailController.EmailRequest();
                emailRequest.setUserEmail(order.getUserEmail());  // user email
                emailController.sendRefundEmail(emailRequest);
                return new ResponseEntity<>("Grąžinimas įvykdytas sėkmingai.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Nepavyko sutvarkyti grąžinio.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Nepavyko sutvarkyti grąžinio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
