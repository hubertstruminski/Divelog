package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.model.Connection;
import com.example.repository.ConnectionRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@CrossOrigin
public class LogoutController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ConnectionRepository connectionRepository;

    @GetMapping("/logout/{email}")
    public ResponseEntity<?> logoutUser(@PathVariable String email) {
        Connection foundedUser = connectionRepository.findByEmail(email);

        foundedUser.setAccessToken(null);
        foundedUser.setAuthenticated(false);

        connectionRepository.save(foundedUser);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
