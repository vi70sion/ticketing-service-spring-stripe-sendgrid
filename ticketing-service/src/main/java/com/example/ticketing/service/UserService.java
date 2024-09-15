package com.example.ticketing.service;

import com.example.ticketing.JwtDecoder;
import com.example.ticketing.model.User;
import com.example.ticketing.repository.UserRepository;
import io.jsonwebtoken.JwtException;

public class UserService {

    UserRepository userRepository = new UserRepository();

    public int checkClient(User user) {
        return userRepository.checkClient(user);
    }




    public boolean authorize(String authorizationHeader){
        try {
            JwtDecoder.decodeJwt(authorizationHeader);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }
    public boolean isTokenCorrect(String authorizationHeader){
        return (authorizationHeader.length() < 20 || authorizationHeader == null || authorizationHeader.isEmpty()) ? false : true;
    }

}
