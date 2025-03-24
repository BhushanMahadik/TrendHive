package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.entities.User;
import com.TrendHive.TrendHive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class OTPService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private AuthenticationManager authenticationManager;


    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY = 5 * 60 * 1000; // 5 minutes in milliseconds
    private final SecureRandom random = new SecureRandom();
    private final Map<String, OTPData> otpStore = new HashMap<>();

    // Inner class to hold OTP and expiry
    private static class OTPData {
        String otp;
        long expiry;


        OTPData(String otp, long expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }


    // Generate a random 6-digit OTP
//    @PreAuthorize("permitAll()")
    public String generateOTP(String username) {
//        String email = "trendhivedev@gmail.com";

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int otp = 100_000 + random.nextInt(900_000); // Ensures 6 digits
        String otpStr = String.valueOf(otp);
        otpStore.put(username, new OTPData(otpStr, System.currentTimeMillis() + OTP_VALIDITY));
//        System.out.println("Generated OTP for " + username + ": " + otpStr); // Replace with email/SMS
        emailService.sendOtpEmail(user.getEmail(),"Generate OTP for "+username+": "+otpStr);
        return otpStr;
    }

    // Verify OTP
//    @PreAuthorize("permitAll()")
    public boolean verifyOTP(String username, String otp) {
        OTPData otpData = otpStore.get(username);
        if (otpData == null) {
            return false;
        }

        boolean isValid = otpData.otp.equals(otp) && System.currentTimeMillis() <= otpData.expiry;
        if (isValid) {
            otpStore.remove(username); // Clear OTP after successful verification
        }
        return isValid;
    }

    // Check if OTP exists for a user
//    @PreAuthorize("permitAll()")
    public boolean hasPendingOTP(String username) {
        return otpStore.containsKey(username);
    }
}