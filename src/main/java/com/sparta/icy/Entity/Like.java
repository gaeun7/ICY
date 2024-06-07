//package com.sparta.icy.Entity;
//
//import com.sparta.icy.Dto.LikeRequestDto;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@Table(name = "like")
//public class Like {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(nullable = false)
//    private Long content_id;
//
//    @Column(nullable = false)
//    private boolean likebtn = false;
//
//    @Column(nullable = false)
//    private Long likecnt = 0L;
//
//    @Column
//    @Enumerated(value = EnumType.STRING)
//    private ContentType contentType;
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime created_at;
//
//    @Column(nullable = false)
//    private LocalDateTime updated_at;
//
//    public Like(LikeRequestDto likeRequestDto) {
//        this.content_id = likeRequestDto.getContent_id();
//        this.likebtn = likeRequestDto.isLikebtn();
//        this.contentType = likeRequestDto.getContentType();
//        this.created_at = LocalDateTime.now();
//        this.updated_at = LocalDateTime.now();
//    }
//}
