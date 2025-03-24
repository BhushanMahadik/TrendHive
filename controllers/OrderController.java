package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Constructor injection (optional, if you prefer over @Autowired)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/user/{userId}/product/{productId}/quantity/{quantity}")
    public ResponseEntity<String> placeOrder(
            @PathVariable int userId,
            @PathVariable int productId,
            @PathVariable int quantity) {
        try {
            String result = orderService.placeOrder(userId, productId, quantity);
            if (result.equals("Product Purchased successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error placing order: " + e.getMessage());
        }
    }

    @PostMapping("/cart/userId/{userId}/productId/{productId}/quantity/{quantity}")
    public ResponseEntity<String> addToCart(@PathVariable int userId,
                                            @PathVariable int productId,
                                            @PathVariable int quantity) {
        try {
            String result = orderService.addToCart(userId, productId, quantity);
            if (result.equals("Product added to the cart")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding to cart: " + e.getMessage());
        }
    }
}