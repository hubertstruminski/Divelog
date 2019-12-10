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

@Controller
@CrossOrigin
public class LogInController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/signin", produces = "application/json")
    public ResponseEntity<?> saveUserData(@RequestBody LoginRequest loginRequest) throws IOException {
        Connection foundedUser = connectionRepository.findByUserIDOrTwitterUserIdOrEmail(loginRequest.getUserID(),
                null, loginRequest.getEmail());
        String jwtToken = null;

        if(foundedUser == null) {
            Connection connection = new Connection();

            setConnection(connection, loginRequest);
            connection.setCreatedAt(new Date());
            connection.setTwitterUserId(null);

            connectionRepository.save(connection);

            jwtToken = jwtTokenProvider.generateToken(connection);
        } else {
            setConnection(foundedUser, loginRequest);
            connectionRepository.save(foundedUser);

            jwtToken = jwtTokenProvider.generateToken(foundedUser);
        }
        return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
    }

    @GetMapping(value = "/getuserdata/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getUserData(@PathVariable String jwtToken) {
        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            return new ResponseEntity<Claims>(claimsFromJwt, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    private void setConnection(Connection foundedUser, LoginRequest loginRequest) {
        foundedUser.setUserID(loginRequest.getUserID());
        foundedUser.setEmail(loginRequest.getEmail());
        foundedUser.setName(loginRequest.getName());
        foundedUser.setAccessToken(loginRequest.getAccessToken());
        foundedUser.setAuthenticated(true);
        foundedUser.setPictureUrl(loginRequest.getPictureUrl());
        foundedUser.setProviderId(Provider.FACEBOOK.getProvider());
        foundedUser.setLoggedAt(new Date());
    }
}