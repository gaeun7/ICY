package com.sparta.icy.Controller;

import com.sparta.icy.Dto.CommentRequestDto;
import com.sparta.icy.Dto.CommentResponseDto;
import com.sparta.icy.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/comments")
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
        CommentResponseDto updatedComment = commentService.updateComment(comments_id, requestDto);
        if (updatedComment != null) {
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{comments_id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long comments_id) {
        return commentService.deleteComment(comments_id);
    }
}
