package com.sparta.icy.repository;

import com.sparta.icy.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
}
