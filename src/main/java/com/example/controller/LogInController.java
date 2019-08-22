package com.example.controller;

import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.LoginRequest;
import com.example.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@Controller
@CrossOrigin
public class LogInController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> saveUserData(@RequestBody LoginRequest loginRequest) throws IOException {

        Connection foundByEmail = connectionRepository.findByEmail(loginRequest.getEmail());

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
        } else {
            foundByEmail.setName(loginRequest.getName());
            foundByEmail.setAccessToken(loginRequest.getAccessToken());
            foundByEmail.setAuthenticated(true);
            foundByEmail.setPictureUrl(loginRequest.getPictureUrl());
            foundByEmail.setLoggedAt(new Date());

            connectionRepository.save(foundByEmail);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/getuserdata/{userID}")
    public ResponseEntity<?> getUserData(@PathVariable String userID) {
        Connection foundByEmail = connectionRepository.findByUserID(Long.valueOf(userID));
        return new ResponseEntity<Connection>(foundByEmail, HttpStatus.OK);
    }
}