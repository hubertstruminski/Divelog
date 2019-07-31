package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class HomeController {

    @GetMapping("/login")
    public String index(Principal user){
        System.out.println(user.getName());
        return "redirect:/user";
    }

}
