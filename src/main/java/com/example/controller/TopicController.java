package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.dto.TopicDto;
import com.example.model.Connection;
import com.example.repository.ConnectionRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ConnectionRepository connectionRepository;

    @PostMapping(name = "/topic")
    public ResponseEntity<?> addTopicToForum(@RequestBody TopicDto topicDto) {
        String jwtToken = topicDto.getJwtToken();
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");

        Connection foundedUser = connectionRepository.findByUserID(userID);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
