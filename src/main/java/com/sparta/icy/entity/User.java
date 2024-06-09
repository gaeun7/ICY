package com.sparta.icy.entity;

import com.sparta.icy.dto.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name="users")
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String intro;

    @Setter
    @Column(nullable = false)
    private String status;

    public User(String username, String nickname, String password, String email, String intro, UserStatus status) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.intro = intro;
        this.status = status.getStatus();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Newsfeed> newsfeeds;
    public void update(UserUpdateRequest req) {
        this.nickname = req.getNickname();
        this.intro = req.getIntro();
        this.password = req.getNewPassword();
    }
}