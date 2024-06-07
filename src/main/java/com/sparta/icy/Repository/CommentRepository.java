package com.sparta.icy.Repository;

import com.sparta.icy.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>  {
    List<Comment> findByNewsfeedId(Long id);
}
