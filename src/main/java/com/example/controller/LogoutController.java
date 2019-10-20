package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.CustomTwitter;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomTwitterRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;

@Controller
@CrossOrigin
public class LogoutController {

    @Autowired
    private CustomTwitterRepository twitterRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @GetMapping("/logout/{email}")
    public ResponseEntity<?> logoutUser(@PathVariable String email) {
        Connection foundedUser = connectionRepository.findByEmailAndAuthenticated(email, true);

        if(foundedUser.getProviderId().equals(Provider.TWITTER.getProvider())) {
            CustomTwitter twitter = twitterRepository.findByUser(foundedUser);

            twitter.setTokenSecret(null);
            twitterRepository.save(twitter);
        }

        foundedUser.setAccessToken(null);
        foundedUser.setAuthenticated(false);

        connectionRepository.save(foundedUser);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
