package com.example.user.app.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.app.domain.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    List<RefreshToken> findByUserId(UUID userId);

    boolean existsByRefreshToken(String refreshToken);

    boolean existsByUserId(UUID userId);

}
