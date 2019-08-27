package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.model.Connection;
import com.example.model.Marker;
import com.example.repository.ConnectionRepository;
import com.example.repository.MarkerRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add/marker/{userID}")
    public ResponseEntity<?> addMarker(@RequestBody Marker marker, @PathVariable Long userID) {
        Connection foundedUser = connectionRepository.findByUserID(userID);
        marker.setUser(foundedUser);
        markerRepository.save(marker);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/get/markers/{jwtToken}")
    public ResponseEntity<?> getAllMarkers(@PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");

        Connection foundedUser = connectionRepository.findByUserID(userID);
        List<Marker> markersList = markerRepository.findAllByUser(foundedUser);

        return new ResponseEntity<List<Marker>>(markersList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/marker/{userID}/{markerID}")
    public ResponseEntity<?> deleteMarker(@PathVariable Long userID, @PathVariable Long markerID) {
        Connection foundedUser = connectionRepository.findByUserID(userID);
        markerRepository.deleteById(markerID);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
