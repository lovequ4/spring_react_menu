package com.example.backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Entity.User;
import com.example.backend.Service.UserService;

@RestController
@RequestMapping("api/")
@CrossOrigin
public class UserController {
    
    private final UserService userService;
  
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
       return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return userService.login(user);
    }

    // private String generateToken(String username) {
    //     // Generate a token based on the user's information
    //     // You can use a library like JWT to create and sign the token

    //     // For demonstration purposes, let's assume a simple token generation
        
    //     return "your-token";
    // }
}



