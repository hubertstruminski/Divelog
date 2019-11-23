package com.example.dto;

import twitter4j.MediaEntity;
import twitter4j.URLEntity;

import java.util.Date;

public class SingleDirectMessage {

    private long id;
    private String text;
    private long senderId;
    private long recipientId;
    private long twitterOwnerId;
    private Date createdAt;
    private URLEntity[] urlEntities;
    private MediaEntity[] mediaEntities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public long getTwitterOwnerId() {
        return twitterOwnerId;
    }

    public void setTwitterOwnerId(long twitterOwnerId) {
        this.twitterOwnerId = twitterOwnerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public URLEntity[] getUrlEntities() {
        return urlEntities;
    }

    public void setUrlEntities(URLEntity[] urlEntities) {
        this.urlEntities = urlEntities;
    }

    public MediaEntity[] getMediaEntities() {
        return mediaEntities;
    }

    public void setMediaEntities(MediaEntity[] mediaEntities) {
        this.mediaEntities = mediaEntities;
    }
}
