package com.example.controller;

import com.example.model.Connection;
import com.example.model.Logbook;
import com.example.model.Marker;
import com.example.repository.LogbookRepository;
import com.example.repository.MarkerRepository;
import com.example.service.ClaimsConverter;
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
    private MarkerRepository markerRepository;

    @Autowired
    private LogbookRepository logbookRepository;

    @Autowired
    private ClaimsConverter claimsConverter;

    @PostMapping(value = "/add/marker/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> addMarker(@RequestBody Marker marker, @PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        marker.setUser(foundedUser);
        markerRepository.save(marker);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/get/markers/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getAllMarkers(@PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        List<Marker> markersList = markerRepository.findAllByUser(foundedUser);

        return new ResponseEntity<List<Marker>>(markersList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/marker/{jwtToken}/{markerID}", produces = "application/json")
    public ResponseEntity<?> deleteMarker(@PathVariable String jwtToken, @PathVariable Long markerID) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        Marker marker = markerRepository.findByIdAndUser(markerID, foundedUser);
        Logbook foundedLogbook = logbookRepository.findByMarker(marker);

        if(foundedLogbook != null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        markerRepository.deleteByIdAndUser(markerID, foundedUser);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
