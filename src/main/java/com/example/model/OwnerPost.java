package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "owner_post")
public class OwnerPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_owner")
    private boolean isOwner;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Connection user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public Connection getUser() {
        return user;
    }

    public void setUser(Connection user) {
        this.user = user;
    }
}
