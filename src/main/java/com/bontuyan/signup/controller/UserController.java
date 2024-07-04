package com.bontuyan.signup.controller;

import com.bontuyan.signup.model.User;
import com.bontuyan.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ApiIgnore
    @RequestMapping(value = "/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @GetMapping("/User")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        // Check for null parameters
        if (user.getUsername() == null || user.getPassword() == null) {
            response.put("message", "Username or Password is null");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Check if user already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            response.put("message", "User already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {

            // Save user to MongoDB
            userRepository.save(user);

            // Successful signup
            response.put("message", "Signup Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}