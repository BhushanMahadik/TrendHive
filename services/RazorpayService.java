//package com.TrendHive.TrendHive.services;
//
//import com.TrendHive.TrendHive.entities.Order;
//import com.TrendHive.TrendHive.repository.OrderRepository;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RazorpayService {
//
//    @Autowired
//    private RazorpayClient razorpayClient;
//
//    @Autowired
//    private OrderRepository orderRepository; // Your Order repository
//
//    @Value("${razorpay.currency}")
//    private String currency;
//
//    public String createPaymentLink(int orderId) throws RazorpayException, JSONException {
//        // Fetch the order from the database
//        Order orderEntity = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//
//        // Get the totalPrice from the Order entity (convert to paise)
//        double totalPrice = orderEntity.getTotalPrice() * 100; // e.g., 500.00 INR becomes 50000 paise
//
//        // Create Razorpay payment link request
//        JSONObject paymentLinkRequest = new JSONObject();
//        paymentLinkRequest.put("amount", (int) totalPrice); // Amount in paise
//        paymentLinkRequest.put("currency", currency);
//        paymentLinkRequest.put("description", "Payment for Order #" + orderId);
//        paymentLinkRequest.put("reference_id", "order_" + orderId);
//
//        // Customer details (optional, can be dynamic based on your Order entity)
//
////        String customerName = orderEntity.getUser().getUserName();
////        String customerEmail = orderEntity.getUser().getEmail();
////        String customerContact = orderEntity.getUser().getContact();
//
////        JSONObject customer = new JSONObject();
////        customer.put("name", "Customer Name"); // Replace with actual customer data if available
////        customer.put("email", "customer@example.com");
////        customer.put("contact", "9999999999");
////        paymentLinkRequest.put("customer", customer);
//
//        // Notification settings (optional)
//        paymentLinkRequest.put("notify", new JSONObject().put("sms", true).put("email", true));
//        paymentLinkRequest.put("reminder_enable", true);
//
//        // Create the payment link
//        com.razorpay.PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
//
//        // Return the short URL for payment
//        return paymentLink.get("short_url");
//
//    }
//}







package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.entities.Order;
import com.TrendHive.TrendHive.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${razorpay.currency}")
    private String currency;


    public String createPaymentLink(int orderId) throws RazorpayException, JSONException {
        // Fetch the order from the database
        Order orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Get the totalPrice from the Order entity (convert to paise)
        double totalPrice = orderEntity.getTotalPrice() * 100; // e.g., 500.00 INR becomes 50000 paise

        // Create Razorpay payment link request
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", (int) totalPrice); // Amount in paise
        paymentLinkRequest.put("currency", currency);
        paymentLinkRequest.put("description", "Payment for Order #" + orderId);

        // Use a unique reference_id by appending a timestamp
        String uniqueReferenceId = "order_" + orderId + "_" + System.currentTimeMillis();
        paymentLinkRequest.put("reference_id", uniqueReferenceId);

        // Customer details (optional, can be dynamic based on your Order entity)
        /*
        String customerName = orderEntity.getUser().getUserName();
        String customerEmail = orderEntity.getUser().getEmail();
        String customerContact = orderEntity.getUser().getContact();

        JSONObject customer = new JSONObject();
        customer.put("name", customerName);
        customer.put("email", customerEmail);
        customer.put("contact", customerContact);
        paymentLinkRequest.put("customer", customer);
        */

        // Notification settings (optional)
        paymentLinkRequest.put("notify", new JSONObject().put("sms", true).put("email", true));
        paymentLinkRequest.put("reminder_enable", true);

        // Create the payment link
        com.razorpay.PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

        // Return the short URL for payment
        return paymentLink.get("short_url");
    }
}