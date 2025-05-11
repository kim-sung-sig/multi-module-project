package com.example.user.app.application.auth.entity;

import com.example.common.util.CommonUtil;
import com.example.common.util.UuidUtil;
import com.example.user.app.application.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    @Setter(AccessLevel.NONE)
    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "refresh_token", length = 512, nullable = false)
    private String refreshToken;

    @Column(name = "expiry_at")
    private Instant expiryAt;

    @PrePersist
    private void onCreate(){
        if (CommonUtil.isEmpty(id)) id = UuidUtil.generate();
    }

    public boolean isExpiringWithinOneMonth() {
        Instant oneMonthLater = Instant.now().plus(30L, ChronoUnit.DAYS);
        return expiryAt.isBefore(oneMonthLater);
    }

    public boolean isExpired() {
        return expiryAt.isBefore(Instant.now());
    }

}
