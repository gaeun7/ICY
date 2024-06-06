package com.sparta.icy.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.icy.Dto.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Length(min = 10, max = 20)
    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @Length(min = 10)
    private String password;

    private String email;

    private String intro;

    private String nickname;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @JsonIgnore
    @CreationTimestamp
    @Builder.Default
    @Column(name = "status_at")
    private LocalDateTime status_at = LocalDateTime.now();

    @JsonIgnore
    @CreationTimestamp
    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime created_at = LocalDateTime.now();

    @JsonIgnore
    @UpdateTimestamp
    @Builder.Default
    @Column(name = "updated_at")
    private LocalDateTime updated_at = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Newsfeed> newsfeeds;

    public User(String username, String password, String email, String intro, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.intro = intro;
        this.nickname = nickname;
        setStatus(Status.ENROLLED);
    }

    public void update(UserUpdateRequest req) {
        this.nickname = req.getNickname();
        this.intro = req.getIntro();
        this.password = req.getNewPassword();
    }
}