package com.example.user.app.application.auth.entity;

import com.example.common.util.CommonUtil;
import com.example.common.util.UuidUtil;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "auth_refresh_token")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RefreshTokenEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    // 연관 관계 맺기용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private User user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "deviceId", column = @Column(name = "device_id")),
            @AttributeOverride(name = "platform", column = @Column(name = "platform")),
            @AttributeOverride(name = "browser", column = @Column(name = "browser"))
    })
    private Device device;

    @Column(name = "token_value", length = 512, nullable = false)
    private String tokenValue;

    @Column(name = "expiry_at")
    private Instant expiryAt;

    @Column(name = "create_by")
    private Instant createBy;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    @PrePersist
    private void onCreate(){
        if (CommonUtil.isEmpty(id)) id = UuidUtil.generate();
    }

    public static RefreshToken toDomain(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(),
                entity.getUserId(),
                entity.getDevice(),
                entity.getTokenValue(),
                entity.getExpiryAt(),
                entity.getCreateBy(),
                entity.getLastUsedAt()
        );
    }

    public static RefreshTokenEntity fromDomain(RefreshToken token) {
        return RefreshTokenEntity.builder()
                .id(token.getId())
                .userId(token.getUserId())
                .device(token.getDevice())
                .tokenValue(token.getTokenValue())
                .expiryAt(token.getExpiryAt())
                .createBy(token.getCreateBy())
                .lastUsedAt(token.getLastUsedAt())
                .build();
    }

}
