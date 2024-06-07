package com.sparta.icy.controller;

import com.sparta.icy.Dto.CommentRequestDto;
import com.sparta.icy.Dto.CommentResponseDto;
import com.sparta.icy.Service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{feed_id}")
    public CommentResponseDto writeComment(@PathVariable Long feed_id, @RequestBody CommentRequestDto requestDto) {
        return commentService.writeComment(feed_id, requestDto);
    }

    @GetMapping("/{feed_id}")
    public List<CommentResponseDto> getComments(@PathVariable Long feed_id) {
        return commentService.getComments(feed_id);
    }

    @PutMapping("/{comments_id}")
    public void updateComment(@PathVariable Long comments_id, @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(comments_id, requestDto);
    }

    @DeleteMapping("/{comments_id}")
    public void deleteComment(@PathVariable Long comments_id) {
        commentService.deleteComment(comments_id);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Long.valueOf(userDetails.getUsername());
    }
}
