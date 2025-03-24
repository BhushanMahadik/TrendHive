package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.entities.*;
import com.TrendHive.TrendHive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CartProductRepository cartProductRepository;

    @Autowired
    private UserService userService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    public String placeOrder(int userId, int productId, int quantity) {

        // Fetch User from DB (Avoid Transient Property Issue)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Fetch Product from DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) return "Product Not Found!....";

        Product product2 = productOpt.get();

        if (product.getStockQuantity() < quantity) return "Insufficient stock";

        if ( quantity == 0) return "please give valid quantity";

        if ( product.getStockQuantity() == 0) return "Product out of stock";

//        Deduct stock
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);



        // Create new Order
        Order order = new Order();
        order.setUser(user);  // Hibernate now knows the user exists
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice()*quantity);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Purchased");

        // Save the Order
        orderRepository.save(order);
        return "Product Purchased successfully";
    }


    public String addToCart(int userId, int productId, int quantity){
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) return "Product not found!...";

        Product product = productOpt.get();

        User user = userService.getById(userId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return "User not found";

        // Fetch or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user, new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        // Check if product is already in the cart
        Optional<CartProduct> existingCartProduct = cartProductRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (existingCartProduct.isPresent()) {
            // Update quantity
            CartProduct cartProduct = existingCartProduct.get();
//            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            int newQuantity = cartProduct.getQuantity() + quantity;
            int newTotalPrice = newQuantity * product.getPrice();
            cartProduct.setQuantity(newQuantity);
            cartProduct.setTotalPrice(newTotalPrice);
            cartProductRepository.save(cartProduct);
        } else {
            // Insert new entry
            int totalPrice = quantity * product.getPrice();
            CartProduct newCartProduct = new CartProduct(cart, product, quantity);
            newCartProduct.setTotalPrice(totalPrice);
            cartProductRepository.save(newCartProduct);
        }

        return "Product added to the cart";
    }
}



