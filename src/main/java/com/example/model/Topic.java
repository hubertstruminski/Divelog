package com.example.model;

import com.example.enums.ForumType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "number_display")
    private int displays;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Connection user;

    @Column(name = "language_forum")
    @Enumerated(EnumType.STRING)
    private ForumType languageForum;

    @OneToMany(mappedBy = "topic")
    private List<Post> posts;

    @OneToMany(mappedBy = "topic")
    private List<CustomFile> files;

    @OneToMany(mappedBy = "topic")
    private List<TopicVote> topicVotes;

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

    public int getDisplays() {
        return displays;
    }

    public void setDisplays(int displays) {
        this.displays = displays;
    }

    public Connection getUser() {
        return user;
    }

    public void setUser(Connection user) {
        this.user = user;
    }

    public ForumType getLanguageForum() {
        return languageForum;
    }

    public void setLanguageForum(ForumType languageForum) {
        this.languageForum = languageForum;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<CustomFile> getFiles() {
        return files;
    }

    public void setFiles(List<CustomFile> files) {
        this.files = files;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<TopicVote> getTopicVotes() {
        return topicVotes;
    }

    public void setTopicVotes(List<TopicVote> topicVotes) {
        this.topicVotes = topicVotes;
    }
}
