package com.example.service;

import com.example.config.JwtTokenProvider;
import com.example.model.Connection;
import com.example.repository.ConnectionRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ClaimsConverter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ConnectionRepository connectionRepository;

    public Connection findUser(String jwtToken) {
        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            Object userID = claimsFromJwt.get("userID");
            Object twitterUserID = claimsFromJwt.get("twitterUserID");
            String email = (String) claimsFromJwt.get("email");

            BigInteger facebookUserId = null;
            BigInteger twitterUserId = null;

            if(userID != null) {
                facebookUserId = BigInteger.valueOf((Long) userID);
            }

            if(twitterUserID != null) {
                twitterUserId = BigInteger.valueOf((Long) twitterUserID);
            }

            return connectionRepository.findByUserIDOrTwitterUserIdOrEmail(facebookUserId, twitterUserId, email);
        }
        return null;
    }
}
