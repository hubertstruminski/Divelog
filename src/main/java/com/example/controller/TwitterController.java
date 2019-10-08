package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.dto.ConnectionDto;
import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.CustomTwitter;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomTwitterRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.math.BigInteger;
import java.util.Date;

@RestController
public class TwitterController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private CustomTwitterRepository twitterRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/signin")
    public String loginWithTwitter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("todfC8BjhF9MbQ7VUeGY8EyWH")
                .setOAuthConsumerSecret("ftDjrAI9KMaZOtYWpg0sZWGx6lqIq4Jhan7uokwMdC2yKHbDj2")
                .setIncludeEmailEnabled(true);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        request.getSession().setAttribute("twitter", twitter);
        try {
            StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");

            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
            request.getSession().setAttribute("requestToken", requestToken);

            response.sendRedirect(requestToken.getAuthenticationURL());

            return "redirect:https://api.twitter.com/oauth/authorize";
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
    }

    @GetMapping("/callback")
    public void loginWithTwitterCallback(HttpServletRequest request, HttpServletResponse response)
            throws TwitterException, ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");

        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
        User user = twitter.verifyCredentials();

        if(accessToken == null || user == null) {
            return;
        }

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndProviderId(
                BigInteger.valueOf(accessToken.getUserId()), user.getEmail(), Provider.TWITTER.getProvider());

        String jwtToken = null;

        if(foundedUser == null) {
            Connection connection = new Connection();
            connection = setUserInfo(connection, accessToken, user);
            connection.setCreatedAt(new Date());

            CustomTwitter customTwitter = new CustomTwitter();

            connectionRepository.save(connection);
            setTwitter(connection, accessToken, customTwitter);

            jwtToken = jwtTokenProvider.generateTokenForTwitter(setConnectionDtoForTwitterToken(connection, customTwitter));
        } else {
            setUserInfo(foundedUser, accessToken, user);
            connectionRepository.save(foundedUser);

            CustomTwitter foundedTwitter = twitterRepository.findByUser(foundedUser);

            if(foundedTwitter == null) {
                CustomTwitter customTwitter = new CustomTwitter();
                setTwitter(foundedUser, accessToken, customTwitter);

                jwtToken = jwtTokenProvider.generateTokenForTwitter(setConnectionDtoForTwitterToken(foundedUser, customTwitter));
            } else {
                setTwitter(foundedUser, accessToken, foundedTwitter);

                jwtToken = jwtTokenProvider.generateTokenForTwitter(setConnectionDtoForTwitterToken(foundedUser, foundedTwitter));
            }

        }
        request.getSession().removeAttribute("requestToken");

        Cookie cookie = new Cookie("twitterJwtToken", jwtToken);
        cookie.setSecure(false);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000/twitter");
    }

    @PostMapping("/twitter/users/search/{searchInput}/{jwtToken}")
    public ResponseEntity<?> findTwitterPeople(@PathVariable String searchInput, @PathVariable String jwtToken) throws TwitterException {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        String accessToken = (String) claimsFromJwt.get("accessToken");
        String tokenSecret = (String) claimsFromJwt.get("tokenSecret");

        Twitter twitter = setTwitterConfiguration(accessToken, tokenSecret);
        ResponseList<User> users = twitter.searchUsers(searchInput, 1);

        return new ResponseEntity<ResponseList<User>>(users, HttpStatus.OK);
    }

    private Connection setUserInfo(Connection connection, AccessToken accessToken, User user) {
        connection.setUserID(BigInteger.valueOf(accessToken.getUserId()));
        connection.setProviderId(Provider.TWITTER.getProvider());
        connection.setEmail(user.getEmail());
        connection.setPictureUrl(user.get400x400ProfileImageURL());
        connection.setLoggedAt(new Date());
        connection.setName(user.getName());
        connection.setAccessToken(accessToken.getToken());
        connection.setAuthenticated(true);

        return connection;
    }

    private void setTwitter(Connection connection, AccessToken accessToken, CustomTwitter customTwitter) {
        customTwitter.setUser(connection);
        customTwitter.setTokenSecret(accessToken.getTokenSecret());
        customTwitter.setScreenName(accessToken.getScreenName());

        twitterRepository.save(customTwitter);
    }

    private ConnectionDto setConnectionDtoForTwitterToken(Connection user, CustomTwitter twitter) {
        ConnectionDto connectionDto = new ConnectionDto();

        connectionDto.setUserID(user.getUserID());
        connectionDto.setProviderId(user.getProviderId());
        connectionDto.setPictureUrl(user.getPictureUrl());
        connectionDto.setName(user.getName());
        connectionDto.setLoggedAt(user.getLoggedAt());
        connectionDto.setEmail(user.getEmail());
        connectionDto.setCreatedAt(user.getCreatedAt());
        connectionDto.setAccessToken(user.getAccessToken());

        connectionDto.setTokenSecret(twitter.getTokenSecret());
        connectionDto.setScreenName(twitter.getScreenName());

        return connectionDto;
    }

    private Twitter setTwitterConfiguration(String accessToken, String tokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("todfC8BjhF9MbQ7VUeGY8EyWH")
                .setOAuthConsumerSecret("ftDjrAI9KMaZOtYWpg0sZWGx6lqIq4Jhan7uokwMdC2yKHbDj2")
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(tokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }
}
