package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.model.Connection;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomFileRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomFileRepository fileRepository;

    @DeleteMapping(value = "/delete/post/file/{fileId}/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> deletePostFileById(@PathVariable Long fileId, @PathVariable String jwtToken) {
        if (jwtTokenProvider.validateToken(jwtToken)) {
            fileRepository.deleteById(fileId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}
