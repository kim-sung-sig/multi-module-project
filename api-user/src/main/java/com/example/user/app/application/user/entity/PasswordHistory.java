package com.example.user.app.application.user.entity;

import com.example.common.util.CommonUtil;
import com.example.common.util.UuidUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "dn_password_history",
    indexes = {
        @Index(name = "idx_password_history_user_id", columnList = "user_id"),
        @Index(name = "idx_password_history_created_at", columnList = "created_at"),
    }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter @ToString @EqualsAndHashCode
public class PasswordHistory {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "password")
    private String password;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    private void onCreate() {
        if (CommonUtil.isEmpty(id)) this.id = UuidUtil.generate();
    }

}
