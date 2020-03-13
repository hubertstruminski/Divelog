package com.example.controller;

import com.example.SocialApplication;
import com.example.util.FilestackDataPicker;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Controller
public class FilestackController {

    private static FilestackDataPicker filestack = null;

    static {
        try {
            filestack = SocialApplication.createFileStackBean();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping(value = "/filestack/file/delete/{handle}")
    public ResponseEntity<?> deleteFile(@PathVariable String handle) {
        RestTemplate restTemplate = new RestTemplate();
        String deleteUrl = "https://www.filestackapi.com/api/file/" + handle +
                "?key=" + filestack.getApiKey() +
                "&policy=" + filestack.getPolicy() +
                "&signature=" + filestack.getSignature();

        ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, String.class);
        return new ResponseEntity<>(response.getStatusCode());
    }
}
