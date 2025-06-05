package com.example.user.app.application.auth.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.common.util.CommonUtil;
import com.example.common.util.UuidUtil;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.user.entity.User;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public RefreshToken toDomain() {
        return new RefreshToken(
                this.getId(),
                this.getUserId(),
                this.getDevice(),
                this.getTokenValue(),
                this.getExpiryAt(),
                this.getCreateBy(),
                this.getLastUsedAt()
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
