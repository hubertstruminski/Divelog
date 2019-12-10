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

import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class LogbookController {

    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private LogbookRepository logbookRepository;

    @Autowired
    private ClaimsConverter claimsConverter;

    @PostMapping(value = "/add/logbook/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> addDiveToLogbook(@RequestBody Logbook logbook, @PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        setTime(logbook, logbook);

        Marker marker = logbook.getMarker();
        marker.setUser(foundedUser);
        markerRepository.save(marker);

        logbook.setUser(foundedUser);
        logbookRepository.save(logbook);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/get/logbook/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getDivesFromLogbook(@PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        List<Logbook> logbooks = logbookRepository.findAllByUser(foundedUser);
        return new ResponseEntity<List<Logbook>>(logbooks, HttpStatus.OK);
    }

    @DeleteMapping(value = "/logbook/{logbookId}/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> deleteLogbookById(@PathVariable Long logbookId, @PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        Logbook logbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);

        if(logbook != null) {
            logbookRepository.deleteById(logbook.getId());

            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/get/logbook/{jwtToken}/{logbookId}", produces = "application/json")
    public ResponseEntity<?> getLogbookById(@PathVariable String jwtToken, @PathVariable Long logbookId) {
        return findLogbookById(jwtToken, logbookId);
    }

    @PutMapping(value = "/edit/logbook/{logbookId}/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> updateLogbookById(@PathVariable Long logbookId, @PathVariable String jwtToken, @RequestBody Logbook logbook) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
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

    @GetMapping(value = "/pdf/logbook/{logbookId}/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getPDFFromLogbookById(@PathVariable Long logbookId, @PathVariable String jwtToken) {
        return findLogbookById(jwtToken, logbookId);
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

    private ResponseEntity<?> findLogbookById(String jwtToken, Long logbookId) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        Logbook logbook = logbookRepository.findByIdAndUser(logbookId, foundedUser);

        if(logbook != null) {
            return new ResponseEntity<Logbook>(logbook, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}
