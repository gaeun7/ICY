package com.sparta.icy.Service;

import com.sparta.icy.Dto.CommentRequestDto;
import com.sparta.icy.Dto.CommentResponseDto;
import com.sparta.icy.Repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponseDto createcomment(CommentRequestDto requestDto) {
        return null;
    }

    public List<CommentResponseDto> getComments(Long id) {
        return null;
    }

    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto) {
        return null;
    }

    public ResponseEntity<?> deleteComment(Long id) {
        return null;
    }
}
