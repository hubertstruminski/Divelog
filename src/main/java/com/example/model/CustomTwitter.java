package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "twitter")
public class CustomTwitter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "token_secret")
    private String tokenSecret;

    @Column(name = "screen_name")
    private String screenName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Connection user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Connection getUser() {
        return user;
    }

    public void setUser(Connection user) {
        this.user = user;
    }
}
