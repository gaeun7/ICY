package com.sparta.icy.Controller;

import com.sparta.icy.Dto.CommentRequestDto;
import com.sparta.icy.Dto.CommentResponseDto;
import com.sparta.icy.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}/comments")
    public CommentResponseDto addComment(@RequestBody CommentRequestDto requestDto) {
        return commentService.createcomment(requestDto);
    }

    @GetMapping("/{id}")
    public List<CommentResponseDto> getCommentsForSchedule(@PathVariable Long id) {
        return commentService.getComments(id);
    }

    @PutMapping("/{id}/comments/{commentsid}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @PathVariable Long commentsid, @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto updatedComment = commentService.updateComment(id, requestDto);
        if (updatedComment != null) {
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/comments/{commentsid}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @PathVariable Long commentsid) {
        return commentService.deleteComment(id);
    }
}
