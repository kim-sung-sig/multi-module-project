package com.example.user.app.application.auth.repository;

import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByTokenValue(String tokenValue);

    List<RefreshTokenEntity> findByUserId(UUID userId);

    boolean existsByTokenValue(String tokenValue);

    boolean existsByUserId(UUID userId);

    void deleteByTokenValue(String tokenValue);
    void deleteByUserId(UUID userId);
}
