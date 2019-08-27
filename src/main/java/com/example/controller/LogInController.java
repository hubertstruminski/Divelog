package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.LoginRequest;
import com.example.repository.ConnectionRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class LogInController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> saveUserData(@RequestBody LoginRequest loginRequest) throws IOException {

        Connection foundByEmail = connectionRepository.findByEmail(loginRequest.getEmail());
        String jwtToken = null;

        if(foundByEmail == null) {
            Connection connection = new Connection();

            connection.setUserID(loginRequest.getUserID());
            connection.setEmail(loginRequest.getEmail());
            connection.setName(loginRequest.getName());
            connection.setAccessToken(loginRequest.getAccessToken());
            connection.setAuthenticated(true);
            connection.setPictureUrl(loginRequest.getPictureUrl());
            connection.setProviderId(Provider.FACEBOOK.getProvider());
            connection.setLoggedAt(new Date());
            connection.setCreatedAt(new Date());

            connectionRepository.save(connection);

            jwtToken = jwtTokenProvider.generateToken(connection);
        } else {
            foundByEmail.setName(loginRequest.getName());
            foundByEmail.setAccessToken(loginRequest.getAccessToken());
            foundByEmail.setAuthenticated(true);
            foundByEmail.setPictureUrl(loginRequest.getPictureUrl());
            foundByEmail.setLoggedAt(new Date());

            connectionRepository.save(foundByEmail);

            jwtToken = jwtTokenProvider.generateToken(foundByEmail);
        }
        return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
    }

    @GetMapping("/getuserdata/{jwtToken}")
    public ResponseEntity<?> getUserData(@PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);

        String accessToken = String.valueOf(claimsFromJwt.get("accessToken"));
        boolean isValidAccessToken = jwtTokenProvider.validateToken(accessToken);

        return new ResponseEntity<Claims>(claimsFromJwt, HttpStatus.OK);
    }
}