package com.example.placement_portal.controller;

import com.example.placement_portal.entity.User;
import com.example.placement_portal.security.JwtTokenProvider;
import com.example.placement_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Login endpoint that combines user authentication with role-based routing
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String email,
            @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Authenticate user
            User user = userService.authenticateUser(email, password);
            
            if (user != null) {
                // Generate JWT token
                String token = jwtTokenProvider.generateToken(user);
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("token", token);
                response.put("email", email);
                response.put("role", user.getRole());
                response.put("name", user.getName());
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // Validate token
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsername(token);
                response.put("valid", true);
                response.put("message", "Token is valid");
                response.put("username", username);
                return ResponseEntity.ok(response);
            } else {
                response.put("valid", false);
                response.put("message", "Invalid token");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Invalid token: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }
}