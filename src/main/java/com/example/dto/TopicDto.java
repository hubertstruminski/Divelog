package com.example.dto;

import com.example.enums.ForumType;

import java.util.List;

public class TopicDto {

    private String title;
    private String message;
    private int likes;
    private ForumType languageForum;
    private String jwtToken;
    private List<FileDto> files;

    public TopicDto() {

    }

    public TopicDto(String title, String message, List<FileDto> files) {
        this.title = title;
        this.message = message;
        this.files = files;
    }

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

    public ForumType getLanguageForum() {
        return languageForum;
    }

    public void setLanguageForum(ForumType languageForum) {
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
