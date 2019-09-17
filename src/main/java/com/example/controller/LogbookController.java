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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

        Date entryTime = logbook.getEntryTime();
        long time = entryTime.getTime();
        time = time - 7200000;
        entryTime.setTime(time);
        logbook.setEntryTime(entryTime);

        Date exitTime = logbook.getExitTime();
        long time2 = exitTime.getTime();
        time2 = time2 - 7200000;
        exitTime.setTime(time2);
        logbook.setExitTime(exitTime);

        Marker marker = logbook.getMarker();
        marker.setUser(foundedUser);
        markerRepository.save(marker);

        logbook.setUser(foundedUser);
        logbookRepository.save(logbook);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/get/logbook/{jwtToken}")
    public ResponseEntity<?> getDivesFromLogbook(@PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");

        Connection foundedUser = connectionRepository.findByUserID(userID);

        List<Logbook> logbooks = logbookRepository.findAllByUser(foundedUser);
        return new ResponseEntity<List<Logbook>>(logbooks, HttpStatus.OK);
    }

    @DeleteMapping("/logbook/{logbookId}/{jwtToken}")
    public ResponseEntity<?> deleteLogbookById(@PathVariable Long logbookId, @PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");

        Connection foundedUser = connectionRepository.findByUserID(userID);

        Logbook logbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);

        if(logbook != null) {
            Marker marker = logbook.getMarker();

            logbookRepository.deleteById(logbook.getId());
            markerRepository.deleteById(marker.getId());

            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/logbook/{jwtToken}/{logbookId}")
    public ResponseEntity<?> getLogbookById(@PathVariable String jwtToken, @PathVariable Long logbookId) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");

        Connection foundedUser = connectionRepository.findByUserID(userID);

        Logbook logbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);

        if(logbook != null) {
            return new ResponseEntity<Logbook>(logbook, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}
