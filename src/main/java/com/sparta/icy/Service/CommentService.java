package com.sparta.icy.Service;

import com.sparta.icy.Dto.CommentRequestDto;
import com.sparta.icy.Dto.CommentResponseDto;
import com.sparta.icy.Entity.Comment;
import com.sparta.icy.Repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponseDto writeComment(CommentRequestDto requestDto) {
        Comment comment = new Comment(requestDto);
        Comment savedcomment = commentRepository.save(comment);
        return new CommentResponseDto(savedcomment);
    }

    public List<CommentResponseDto> getComments(Long feed_id) {
        List<Comment> comments = commentRepository.findByNewsfeedId(feed_id);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return null;
        }
        comment.setContent(requestDto.getContent());
        comment.setUpdated_at(LocalDateTime.now()); // 수정
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    public ResponseEntity<?> deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        commentRepository.delete(comment);
        return ResponseEntity.ok().build();
    }
}
