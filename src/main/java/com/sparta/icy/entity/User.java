package com.sparta.icy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.icy.dto.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @Length(min = 10, max = 20)
    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @Length(min = 10)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false) // 중복 x
    private String email;

    @Column(nullable = false)
    private String intro;

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

    public void update(UserUpdateRequest req) {
        this.nickname = req.getNickname();
        this.intro = req.getIntro();
        this.password = req.getNewPassword();
    }
}
