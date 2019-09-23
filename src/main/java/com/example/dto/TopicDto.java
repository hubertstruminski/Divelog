package com.example.dto;

import java.util.List;

public class TopicDto {

    private String title;
    private String message;
    private int likes;
    private String languageForum;
    private String jwtToken;
    private List<FileDto> files;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getLanguageForum() {
        return languageForum;
    }

    public void setLanguageForum(String languageForum) {
        this.languageForum = languageForum;
    }

    public List<FileDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileDto> files) {
        this.files = files;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
