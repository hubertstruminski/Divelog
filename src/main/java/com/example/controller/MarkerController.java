package com.example.controller;

import com.example.model.Marker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@CrossOrigin
public class MarkerController {

    @PostMapping("/add/marker")
    public ResponseEntity<?> addMarker(@RequestBody Marker marker, Principal principal) {
        System.out.println(marker.getName());
        System.out.println(marker.getLatitude());
        System.out.println(marker.getLongitude());

        System.out.println(principal.getName());

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
