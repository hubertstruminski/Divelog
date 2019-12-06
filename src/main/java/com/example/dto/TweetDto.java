package com.example.dto;

import java.util.List;

public class TweetDto {

    private String message;
    private List<TweetFileDto> files;

    public TweetDto() {

    }

    public TweetDto(String message, List<TweetFileDto> files) {
        this.message = message;
        this.files = files;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TweetFileDto> getFiles() {
        return files;
    }

    public void setFiles(List<TweetFileDto> files) {
        this.files = files;
    }
}
