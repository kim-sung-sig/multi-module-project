package com.example.user.domain.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.common.util.UuidUtil;
import com.example.user.domain.entity.convertor.UserRoleConverter;
import com.example.user.domain.entity.convertor.UserStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "dn_user",
    indexes = {
        @Index(name = "idx_user_username", columnList = "username", unique = true),
        @Index(name = "idx_user_nickname", columnList = "nick_name", unique = true),
        @Index(name = "idx_user_email", columnList = "email"),
    }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter @ToString @EqualsAndHashCode
public class User {

    @Id
    private UUID id;

    // security
    @Column(name = "username", nullable = false, unique = true)
    @Comment("아이디")
    private String username;

    @Column(name = "password")
    @Comment("비밀번호")
    private String password;

    @Column(name = "role", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    @Comment("사용자 권한")
    private UserRole role;

    @Column(name = "status", nullable = false)
    @Convert(converter = UserStatusConverter.class)
    @Comment("사용자 상태")
    private UserStatus status;

    @Column(name = "login_fail_count", columnDefinition = "int default 0")
    @Comment("로그인 실패 횟수")
    private int loginFailCount;
    

    @Column(name = "last_login_at")
    @Comment("마지막 로그인 일시")
    private LocalDateTime lastLoginAt;

    @Column(name = "temp_password")
    @Comment("임시 비밀번호")
    private String tempPassword;

    @Column(name = "temp_password_expired_at")
    @Comment("임시 비밀번호 만료 일시")
    private LocalDateTime tempPasswordExpiredAt;


    // 사용자 정보
    @Column(name = "name", nullable = false)
    @Comment("사용자 이름")
    private String name;

    @Column(name = "nick_name", nullable = false)
    @Comment("사용자 닉네임")
    private String nickName;

    @Column(name = "nick_name_tag", nullable = false, unique = true)
    @Comment("사용자 닉네임 테그")
    private String nickNameTag;

    @Column(name = "email")
    @Comment("사용자 이메일")
    private String email;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    @Comment("생성자")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Comment("생성 일시")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    @Comment("수정자")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Comment("수정 일시")
    private Instant updatedAt;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UuidUtil.generate();
        }
    }
}
