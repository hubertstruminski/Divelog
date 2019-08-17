package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@CrossOrigin
public class HomeController {

    @GetMapping("/login/facebook")
    public String index(Principal user){
        System.out.println(user.getName());
        return "redirect:/user";
    }

}