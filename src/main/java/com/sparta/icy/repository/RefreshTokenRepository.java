package com.sparta.icy.repository;

import com.sparta.icy.entity.RefreshToken;
import com.sparta.icy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUser(User user);
}
