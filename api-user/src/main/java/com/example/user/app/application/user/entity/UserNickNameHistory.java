package com.example.user.app.application.user.entity;

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
@Table(name = "dn_user_nick_name_history")
@Getter @ToString @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserNickNameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.NONE)
    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "tag", nullable = false)
    private String tag;

    @Column(name = "create_at") @CreatedDate
    private Instant createAt;

    @Column(name = "last_use_at")
    private Instant lastUseAt;

}
