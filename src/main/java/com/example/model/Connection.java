package com.example.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    protected BigInteger userID;

    @NotNull
    protected String email;

    @NotNull
    protected String name;

    @Column(name = "access_token")
    protected String accessToken;

    @NotNull
    private boolean authenticated;

    @Column(name = "picture_url")
    protected String pictureUrl;

    @NotNull
    protected String providerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "logged_in_at")
    @NotNull
    protected Date loggedAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    @NotNull
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

    public Connection() {

    }

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
}
