package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.model.Connection;
import com.example.model.Logbook;
import com.example.model.Marker;
import com.example.repository.ConnectionRepository;
import com.example.repository.LogbookRepository;
import com.example.repository.MarkerRepository;
import com.example.service.MapErrorValidatorService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
@CrossOrigin
public class LogbookController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private LogbookRepository logbookRepository;

    @PostMapping("/add/logbook/{jwtToken}")
    public ResponseEntity<?> addDiveToLogbook(@RequestBody Logbook logbook, @PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");

        Connection foundedUser = connectionRepository.findByUserID(userID);

        Marker marker = logbook.getMarker();
        marker.setUser(foundedUser);
        markerRepository.save(marker);

        logbook.setUser(foundedUser);
        logbookRepository.save(logbook);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
