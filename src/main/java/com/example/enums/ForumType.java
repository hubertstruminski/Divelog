package com.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ForumType {

    @JsonProperty("english")
    ENGLISH("english"),

    @JsonProperty("polish")
    POLISH("polish"),

    @JsonProperty("germany")
    GERMANY("germany");

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
