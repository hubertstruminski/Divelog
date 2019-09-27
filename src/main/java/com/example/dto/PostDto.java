package com.example.dto;

import java.util.List;

public class PostDto {

    private String message;
    private String languageForum;
    private Long topicId;
    private String jwtToken;
    private List<FileDto> files;
    private boolean isPostOwner;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLanguageForum() {
        return languageForum;
    }

    public void setLanguageForum(String languageForum) {
        this.languageForum = languageForum;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public List<FileDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileDto> files) {
        this.files = files;
    }

    public boolean isPostOwner() {
        return isPostOwner;
    }

    public void setPostOwner(boolean postOwner) {
        isPostOwner = postOwner;
    }
}
