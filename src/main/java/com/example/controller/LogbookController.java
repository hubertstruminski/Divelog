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

import java.util.Date;
import java.util.List;

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
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);

        setTime(logbook, logbook);

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
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);

        List<Logbook> logbooks = logbookRepository.findAllByUser(foundedUser);
        return new ResponseEntity<List<Logbook>>(logbooks, HttpStatus.OK);
    }

    @DeleteMapping("/logbook/{logbookId}/{jwtToken}")
    public ResponseEntity<?> deleteLogbookById(@PathVariable Long logbookId, @PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);

        Logbook logbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);

        if(logbook != null) {
            logbookRepository.deleteById(logbook.getId());

            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/logbook/{jwtToken}/{logbookId}")
    public ResponseEntity<?> getLogbookById(@PathVariable String jwtToken, @PathVariable Long logbookId) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);

        Logbook logbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);

        if(logbook != null) {
            return new ResponseEntity<Logbook>(logbook, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/edit/logbook/{logbookId}/{jwtToken}")
    public ResponseEntity<?> updateLogbookById(@PathVariable Long logbookId, @PathVariable String jwtToken, @RequestBody Logbook logbook) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);

        Logbook foundedLogbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);
        Marker marker = foundedLogbook.getMarker();

        if(foundedLogbook != null) {
            setTime(logbook, foundedLogbook);
            foundedLogbook.setDivingSuit(logbook.getDivingSuit());
            foundedLogbook.setAirTemperature(logbook.getAirTemperature());
            foundedLogbook.setAverageDepth(logbook.getAverageDepth());
            foundedLogbook.setBallast(logbook.getBallast());
            foundedLogbook.setComment(logbook.getComment());
            foundedLogbook.setCylinderCapacity(logbook.getCylinderCapacity());
            foundedLogbook.setDivingType(logbook.getDivingType());
            foundedLogbook.setGlovesType(logbook.getGlovesType());
            foundedLogbook.setMaxDepth(logbook.getMaxDepth());
            foundedLogbook.setPartnerName(logbook.getPartnerName());
            foundedLogbook.setPartnerSurname(logbook.getPartnerSurname());
            foundedLogbook.setVisibility(logbook.getVisibility());
            foundedLogbook.setWaterEntryType(logbook.getWaterEntryType());
            foundedLogbook.setWaterTemperature(logbook.getWaterTemperature());
            foundedLogbook.setWaterType(logbook.getWaterType());

            marker.setName(logbook.getMarker().getName());
            marker.setLatitude(logbook.getMarker().getLatitude());
            marker.setLongitude(logbook.getMarker().getLongitude());

            foundedLogbook.setMarker(marker);

            markerRepository.save(marker);
            logbookRepository.save(foundedLogbook);

            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/pdf/logbook/{logbookId}/{jwtToken}")
    public ResponseEntity<?> getPDFFromLogbookById(@PathVariable Long logbookId, @PathVariable String jwtToken) {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Long userID = (Long) claimsFromJwt.get("userID");
        String email = (String) claimsFromJwt.get("email");

        Connection foundedUser = connectionRepository.findByUserIDAndEmailAndAuthenticated(userID, email, true);

        Logbook foundedLogbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);

        if(foundedLogbook != null) {
            return new ResponseEntity<Logbook>(foundedLogbook, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    private void setTime(Logbook logbook, Logbook foundedLogbook) {
        Date entryTime = logbook.getEntryTime();
        long time = entryTime.getTime();
        time = time - 7200000;
        entryTime.setTime(time);
        foundedLogbook.setEntryTime(entryTime);

        Date exitTime = logbook.getExitTime();
        long time2 = exitTime.getTime();
        time2 = time2 - 7200000;
        exitTime.setTime(time2);
        foundedLogbook.setExitTime(exitTime);
    }
}
