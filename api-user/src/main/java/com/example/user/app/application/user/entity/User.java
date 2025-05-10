package com.example.user.app.application.user.entity;

import java.time.Instant;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.common.util.UuidUtil;
import com.example.user.app.application.user.entity.converter.UserRoleConverter;
import com.example.user.app.application.user.entity.converter.UserStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dn_user")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {

    @Id
    private UUID id;

    // security
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRole role;

    @Column(name = "status", nullable = false)
    @Convert(converter = UserStatusConverter.class)
    private UserStatus status;

    @Column(name = "login_fail_count")
    private Integer loginFailCount;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "temp_password")
    private String tempPassword;

    @Column(name = "temp_password_expired_at")
    private Instant tempPasswordExpiredAt;

    // 사용자 정보
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "nick_name_tag", nullable = false)
    private Long nickNameTag;

    @Column(name = "email")
    private String email;

    // audit
    @Column(name = "created_by", updatable = false) @CreatedBy
    private String createdBy;

    @Column(name = "created_at", updatable = false) @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_by") @LastModifiedBy
    private String updatedBy;

    @Column(name = "updated_at") @LastModifiedDate
    private Instant updatedAt;

//    public void changeNickName(NickNameTag nickNameTag) {
//        this.nickName = nickNameTag.getNickName().get();
//        this.nickNameTag = nickNameTag.getTag();
//    }

    @PrePersist
    public void onCreate() {
        if (this.id == null) this.id = UuidUtil.generate();
        if (this.loginFailCount == null) this.loginFailCount = 0;
    }
}
