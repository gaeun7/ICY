package com.sparta.icy.Controller;

import com.sparta.icy.Dto.CommentRequestDto;
import com.sparta.icy.Dto.CommentResponseDto;
import com.sparta.icy.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponseDto writeComment(@RequestBody CommentRequestDto requestDto) {
        return commentService.writeComment(requestDto);
    }

    @GetMapping("/{feed_id}")
    public List<CommentResponseDto> getComments(@PathVariable Long feed_id) {
        return commentService.getComments(feed_id);
    }

    @PutMapping("/{comments_id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long comments_id, @RequestBody CommentRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = getUserIdFromAuthentication(authentication);
        CommentResponseDto updatedComment = commentService.updateComment(comments_id, requestDto, currentUserId);
        if (updatedComment != null) {
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{comments_id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long comments_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = getUserIdFromAuthentication(authentication);
        return commentService.deleteComment(comments_id, currentUserId);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Long.valueOf(userDetails.getUsername());
    }
}
