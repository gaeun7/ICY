//package com.sparta.icy.Controller;
//
//import com.sparta.icy.Service.LikeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping
//public class LikeController {
//
//    private final LikeService likeService;
//
//    @Autowired
//    public LikeController(LikeService likeService) {
//        this.likeService = likeService;
//    }
//
//    @PutMapping("/{contentType}/{contentId}/like/{likeId}")
//    public ResponseEntity<?> toggleLike(@PathVariable String contentType,
//                                        @PathVariable Long contentId,
//                                        @PathVariable Long likeId) {
//        try {
//            likeService.toggleLike(contentType, contentId, likeId);
//            return ResponseEntity.ok().build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}