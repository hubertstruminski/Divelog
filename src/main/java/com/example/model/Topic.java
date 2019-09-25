package com.example.model;

import org.hibernate.annotations.Type;
import java.util.Date;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "topic_title")
    private String title;

    @Column(name = "message")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String message;

    @Column(name = "number_likes")
    private int likes;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Connection user;

    @ManyToOne
    @JoinColumn(name = "language_forum_id")
    private LanguageForum languageForum;

    @OneToMany(mappedBy = "topic")
    private List<CustomFile> files;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Connection getUser() {
        return user;
    }

    public void setUser(Connection user) {
        this.user = user;
    }

    public LanguageForum getLanguageForum() {
        return languageForum;
    }

    public void setLanguageForum(LanguageForum languageForum) {
        this.languageForum = languageForum;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
