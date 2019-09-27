package com.example.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "message")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String message;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Connection user;

    @Column(name = "owner_post")
    private boolean isPostOwner;

    @ManyToOne
    @JoinColumn(name = "language_forum_id")
    private LanguageForum languageForum;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "post")
    private List<CustomFile> files;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Connection getUser() {
        return user;
    }

    public void setUser(Connection user) {
        this.user = user;
    }

    public boolean isPostOwner() {
        return isPostOwner;
    }

    public void setPostOwner(boolean postOwner) {
        isPostOwner = postOwner;
    }

    public LanguageForum getLanguageForum() {
        return languageForum;
    }

    public void setLanguageForum(LanguageForum languageForum) {
        this.languageForum = languageForum;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<CustomFile> getFiles() {
        return files;
    }

    public void setFiles(List<CustomFile> files) {
        this.files = files;
    }
}
