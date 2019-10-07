package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.model.Connection;
import com.example.model.Logbook;
import com.example.model.Marker;
import com.example.repository.ConnectionRepository;
import com.example.repository.LogbookRepository;
import com.example.repository.MarkerRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@Controller
@CrossOrigin
public class MarkerController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LogbookRepository logbookRepository;

    @PostMapping("/add/marker/{jwtToken}")
    public ResponseEntity<?> addMarker(@RequestBody Marker marker, @PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        BigInteger userID = (BigInteger) claimsFromJwt.get("userID");
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);
        marker.setUser(foundedUser);

        markerRepository.save(marker);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/get/markers/{jwtToken}")
    public ResponseEntity<?> getAllMarkers(@PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        BigInteger userID = (BigInteger) claimsFromJwt.get("userID");
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);
        List<Marker> markersList = markerRepository.findAllByUser(foundedUser);

        return new ResponseEntity<List<Marker>>(markersList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/marker/{jwtToken}/{markerID}")
    public ResponseEntity<?> deleteMarker(@PathVariable String jwtToken, @PathVariable Long markerID) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        BigInteger userID = (BigInteger) claimsFromJwt.get("userID");
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);

        Marker marker = markerRepository.findByIdAndUser(markerID, foundedUser);
        Logbook foundedLogbook = logbookRepository.findByMarker(marker);

        if(foundedLogbook != null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        markerRepository.deleteByIdAndUser(markerID, foundedUser);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
