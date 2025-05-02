package com.example.user.app.domain.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.common.util.CommonUtil;
import com.example.common.util.UuidUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "auth_refresh_token"
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter @ToString @EqualsAndHashCode
public class RefreshToken {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "refresh_token", length = 512, nullable = false)
    private String refreshToken;

    @Column(name = "expiry_at")
    private Instant expiryAt;

    @PrePersist
    private void onCreate(){
        if (CommonUtil.isEmpty(id)) id = UuidUtil.generate();
    }
}
