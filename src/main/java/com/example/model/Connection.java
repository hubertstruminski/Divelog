package com.example.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Digits(integer = 24, fraction = 0)
    @Column(name = "user_id")
    protected BigInteger userID;

    @Digits(integer = 24, fraction = 0)
    @Column(name = "twitter_user_id")
    protected BigInteger twitterUserId;

    @NotNull
    @Column(name = "email")
    protected String email;

    @NotNull
    @Column(name = "name")
    protected String name;

    @Column(name = "access_token")
    protected String accessToken;

    @NotNull
    @Column(name = "is_authenticated")
    private boolean authenticated;

    @Column(name = "picture_url")
    protected String pictureUrl;

    @NotNull
    @Column(name = "provider_id")
    protected String providerId;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "logged_at")
    protected Date loggedAt;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "created_at")
    protected Date createdAt;

    @OneToMany(mappedBy = "user")
    private List<Marker> markers;

    @OneToMany(mappedBy = "user")
    private List<Logbook> logbooks;

    @OneToMany(mappedBy = "user")
    private List<Topic> topics;

    @OneToMany(mappedBy = "user")
    private List<TopicVote> topicVotes;

    @OneToOne(mappedBy = "user")
    private CustomTwitter customTwitter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUserID() {
        return userID;
    }

    public void setUserID(BigInteger userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Date getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(Date loggedAt) {
        this.loggedAt = loggedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigInteger getTwitterUserId() {
        return twitterUserId;
    }

    public void setTwitterUserId(BigInteger twitterUserId) {
        this.twitterUserId = twitterUserId;
    }
}
