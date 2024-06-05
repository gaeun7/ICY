package com.sparta.icy.Entity;


import com.sparta.icy.Dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id2", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Newsfeed newsfeed;

    @Column(nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updated_at;

    @Column(nullable = false)
    private Long like;

    public Comment(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
        this.created_at = commentRequestDto.getCreated_at();
        this.updated_at = commentRequestDto.getUpdated_at();
        this.like = commentRequestDto.getLike();
    }

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
