package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldControllerTest {

    @GetMapping("hello-world")
    public String helloWorld() {
        return "Hello World";
    }
}
