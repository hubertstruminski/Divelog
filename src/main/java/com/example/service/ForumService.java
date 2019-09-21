package com.example.service;

import com.example.enums.ForumType;
import com.example.model.LanguageForum;
import com.example.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    public void initForum() {
        LanguageForum englishForum = new LanguageForum();
        englishForum.setForumType(ForumType.ENGLISH);

        forumRepository.save(englishForum);

        LanguageForum germanyForum = new LanguageForum();
        germanyForum.setForumType(ForumType.GERMANY);

        forumRepository.save(germanyForum);

        LanguageForum polishForum = new LanguageForum();
        polishForum.setForumType(ForumType.POLISH);

        forumRepository.save(polishForum);
    }
}
