package com.example.controller;

import com.example.model.Connection;
import com.example.model.Marker;
import com.example.repository.ConnectionRepository;
import com.example.repository.MarkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin
public class MarkerController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private MarkerRepository markerRepository;

    @PostMapping("/add/marker/{userID}")
    public ResponseEntity<?> addMarker(@RequestBody Marker marker, @PathVariable Long userID) {
        Connection foundedUser = connectionRepository.findByUserID(userID);
        marker.setUser(foundedUser);
        markerRepository.save(marker);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
