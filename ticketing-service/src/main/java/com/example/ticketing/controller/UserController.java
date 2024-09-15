package com.example.ticketing.controller;

import com.example.ticketing.JwtGenerator;
import com.example.ticketing.model.User;
import com.example.ticketing.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    UserService userService = new UserService();
    public UserController() {
    }

    @CrossOrigin
    @PostMapping("/ticketing/user/login")
    public ResponseEntity <Map<String, String>> checkClient(@RequestBody User user) {
        int userId = userService.checkClient(user);
        if (userId == -1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String jwtToken = JwtGenerator.generateJwt(userId);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
