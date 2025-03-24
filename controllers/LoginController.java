package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.entities.User;
import com.TrendHive.TrendHive.repository.UserRepository;
import com.TrendHive.TrendHive.services.OTPService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;

//    @Autowired
//    private AuthenticationManager authenticationManager;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    // Step 1: Login with username and password, generate OTP
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username()).orElse(null);
        if (user == null || !user.getPassword().equals(loginRequest.password())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        // Generate OTP (stored in memory, not DB)
        otpService.generateOTP(user.getUsername());
        return ResponseEntity.ok("OTP generated and sent. Please verify.");
    }

    // Step 2: Verify OTP and grant access
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OTPRequest otpRequest) {
        User user = userRepository.findByUsername(otpRequest.username()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        if (!otpService.hasPendingOTP(otpRequest.username())) {
            return ResponseEntity.status(401).body("No OTP generated for this user");
        }

        if (otpService.verifyOTP(otpRequest.username(), otpRequest.otp())) {
            return ResponseEntity.ok("Login successful! User can now access the application.");
        } else {
            return ResponseEntity.status(401).body("Invalid or expired OTP");
        }
    }

    // Example protected endpoint after successful login
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard(@RequestParam String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok("Welcome to the dashboard, " + username + "!");
    }
}

// Request DTOs

record LoginRequest(String username, String password) {}
record OTPRequest(String username, String otp) {}