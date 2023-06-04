package com.example.backend.Service;

import com.example.backend.Entity.User;
import com.example.backend.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> register(@RequestBody User user) {
        // Validate registration request
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already exist.");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required.");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        // Create user object
        user.setUsername(user.getUsername());
        
        // Encrypt the password
        user.setPassword(user.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        
        // Save user to the database
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully.");
    }

    public ResponseEntity<String> login(@RequestBody User user) {
        // Find user by username
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            User UserfromDB = optionalUser.get(); //儲存找到的密碼 放到UserfromDB變數

            // Validate password
            if (passwordEncoder.matches(user.getPassword(), UserfromDB.getPassword())) {
                // Generate and return token
                // String token = generateToken(user.getUsername());
                // return ResponseEntity.ok("User login success");
                
                // Create a map to include the user ID in the response
                Map<String, Object> response = new HashMap<>();
                response.put("message", "User login success");
                response.put("userId", UserfromDB.getId());

               // Convert the map to a JSON string
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String responseBody = objectMapper.writeValueAsString(response);
                    return ResponseEntity.ok(responseBody);
                } catch (JsonProcessingException e) {
                    // Handle JSON processing exception
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
                }
            }
        }

        // Invalid username or password
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
    }
}
