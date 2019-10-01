package com.example.dto;

public class TopicTemplate {

    private int numberDisplay;
    private int numberComments;
    private int likes;
    private int vote;

    public TopicTemplate(int numberDisplay, int numberComments, int likes, int vote) {
        this.numberDisplay = numberDisplay;
        this.numberComments = numberComments;
        this.likes = likes;
        this.vote = vote;
    }

    public int getNumberDisplay() {
        return numberDisplay;
    }

    public void setNumberDisplay(int numberDisplay) {
        this.numberDisplay = numberDisplay;
    }

    public int getNumberComments() {
        return numberComments;
    }

    public void setNumberComments(int numberComments) {
        this.numberComments = numberComments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
