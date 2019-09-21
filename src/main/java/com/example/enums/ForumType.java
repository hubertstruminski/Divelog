package com.example.enums;

public enum ForumType {

    ENGLISH("ENGLISH"),
    POLISH("POLISH"),
    GERMANY("GERMANY");

    private String forumName;

    ForumType(String forumName) {
        this.forumName = forumName;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }
}
