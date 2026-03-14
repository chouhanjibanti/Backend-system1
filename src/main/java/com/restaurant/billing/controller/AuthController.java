package com.restaurant.billing.controller;

import com.restaurant.billing.dto.LoginRequest;
import com.restaurant.billing.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173", "http://127.0.0.1:3000"})

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
// @CrossOrigin(origins = {"https://frontend-system1.vercel.app"})
public class AuthController {

    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            boolean isValid = authService.validateLogin(loginRequest.getUsername(), loginRequest.getPassword());
            
            if (isValid) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Login successful");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Invalid username or password");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login failed");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser() {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("username", "user"); // Simple response for now
            response.put("role", "USER");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to get current user");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout failed");
            return ResponseEntity.status(500).body(response);
        }
    }
}
