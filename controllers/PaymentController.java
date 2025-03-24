package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.services.RazorpayService;
import com.razorpay.RazorpayException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private RazorpayService paymentService;

    @PostMapping("/create-payment-link/{orderId}")
    public ResponseEntity<String> createPaymentLink(@PathVariable int orderId) {
        try {
            String paymentLink = paymentService.createPaymentLink(orderId);
            return ResponseEntity.ok(paymentLink); // Returns the payment link (e.g., https://rzp.io/l/abc123)
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Error creating payment link: " + e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        // Parse the payload and verify the signature
        // Update Order status in the database
        return ResponseEntity.ok("Webhook received");
    }
}