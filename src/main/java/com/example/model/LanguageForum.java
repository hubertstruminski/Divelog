package com.example.model;

import com.example.enums.ForumType;

import javax.persistence.*;

@Entity
@Table(name = "language_forum")
public class LanguageForum {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ForumType forumType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ForumType getForumType() {
        return forumType;
    }

    public void setForumType(ForumType forumType) {
        this.forumType = forumType;
    }
}
