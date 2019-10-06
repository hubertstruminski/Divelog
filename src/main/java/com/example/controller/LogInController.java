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

    @PostMapping("/signin")
    public ResponseEntity<?> saveUserData(@RequestBody LoginRequest loginRequest) throws IOException {
        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndProviderId(loginRequest.getUserID(), loginRequest.getEmail(), Provider.FACEBOOK.getProvider());
        String jwtToken = null;

        if(foundedUser == null) {
            Connection connection = new Connection();

            setConnection(connection, loginRequest);
            connection.setCreatedAt(new Date());

            connectionRepository.save(connection);

            jwtToken = jwtTokenProvider.generateToken(connection);
        } else {
            setConnection(foundedUser, loginRequest);
            connectionRepository.save(foundedUser);

            jwtToken = jwtTokenProvider.generateToken(foundedUser);
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