package com.example.user.app.infra.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.app.infra.entity.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    List<RefreshTokenEntity> findByUserId(UUID userId);

    boolean existsByRefreshToken(String refreshToken);

    boolean existsByUserId(UUID userId);

}
