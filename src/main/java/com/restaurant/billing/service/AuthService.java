package com.restaurant.billing.service;

import com.restaurant.billing.entity.User;
import com.restaurant.billing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Simple login validation - checks username and password against database
     */
    public boolean validateLogin(String username, String password) {
        try {
            log.info("Attempting login for user: {}", username);
            
            // Find user by username
            User user = userRepository.findByUsername(username);
            
            if (user == null) {
                log.warn("User not found: {}", username);
                return false;
            }
            
            // Check if user is active
            if (!user.getActive()) {
                log.warn("User is inactive: {}", username);
                return false;
            }
            
            // Validate password
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            
            if (passwordMatches) {
                log.info("Login successful for user: {}", username);
                return true;
            } else {
                log.warn("Invalid password for user: {}", username);
                return false;
            }
            
        } catch (Exception e) {
            log.error("Login validation error for user {}: {}", username, e.getMessage());
            return false;
        }
    }
}
