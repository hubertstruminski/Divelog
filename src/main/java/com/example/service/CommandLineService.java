package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineService implements CommandLineRunner {

    @Autowired
    private ForumService forumService;

    @Override
    public void run(String... args) throws Exception {
        forumService.initForum();
    }
}
