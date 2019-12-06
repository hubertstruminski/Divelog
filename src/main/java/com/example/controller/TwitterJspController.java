package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.dto.ConnectionDto;
import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.CustomTwitter;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomTwitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

@CrossOrigin
@Controller
public class TwitterJspController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomTwitterRepository twitterRepository;


}
