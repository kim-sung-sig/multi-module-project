package com.example.user.app.domain.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Table(name = "dn_nick_name_history")
@Getter @ToString @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class NickNameHistory {

    @Id
    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "seq")
    private Long seq;

    @Column(name = "created_at") @CreatedDate
    private Instant createdAt;

    @Column(name = "created_by") @CreatedBy
    private String createdBy;       // username 과 일치

    @Column(name = "updated_at") @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "updated_by") @LastModifiedBy
    private String updatedBy;       // username 과 일치

    public void incrementSeq() {
        this.seq++;
    }

    @PrePersist
    private void onPrePersist() {
        if (this.seq == null) this.seq = 1L;
    }

}
