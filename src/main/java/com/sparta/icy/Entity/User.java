package com.sparta.icy.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String intro;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    @Column(nullable = false)
    private Date state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updated_at;

    @Column(nullable = false)
    private String user_id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Newsfeed> newsfeeds;

    @PrePersist
    protected void onCreate() {
        created_at = new Date();
        updated_at = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = new Date();
    }
}
