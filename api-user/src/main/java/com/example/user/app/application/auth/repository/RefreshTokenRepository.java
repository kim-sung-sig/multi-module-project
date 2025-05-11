package com.example.user.app.application.auth.repository;

import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    List<RefreshTokenEntity> findByUserId(UUID userId);

    boolean existsByRefreshToken(String refreshToken);

    boolean existsByUserId(UUID userId);

    void deleteByRefreshToken(String refreshToken);
    void deleteByUserId(UUID userId);
}
