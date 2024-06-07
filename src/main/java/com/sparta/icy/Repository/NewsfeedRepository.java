package com.sparta.icy.Repository;

import com.sparta.icy.Entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
}
