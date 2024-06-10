//package com.sparta.icy.Service;
//
//import com.sparta.icy.Entity.Like;
//import com.sparta.icy.Repository.LikeRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class LikeService {
//
//    private final LikeRepository likeRepository;
//
//    @Autowired
//    public LikeService(LikeRepository likeRepository) {
//        this.likeRepository = likeRepository;
//    }
//
//    @Transactional
//    public void toggleLike(String contentType, Long contentId, Long likeId) {
//        Like like = likeRepository.findById(likeId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 Like가 존재하지 않습니다."));
//
//        if (like.isLikebtn()) {
//            like.setLikebtn(false);
//            like.setLikecnt(like.getLikecnt() + 1);
//        } else {
//            like.setLikebtn(true);
//            like.setLikecnt(like.getLikecnt() - 1);
//        }
//
//        likeRepository.save(like);
//    }
//}
