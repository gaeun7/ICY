package com.sparta.icy.Repository;

import com.sparta.icy.Entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {
    List<Comment> findAllByOrderByCreatedAtDesc();
}
