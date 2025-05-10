package com.example.user.app.application.nickname;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.user.app.application.nickname.domain.NickName;
import com.example.user.app.application.nickname.domain.NickNameTag;

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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter @ToString @EqualsAndHashCode
public class NickNameStorage {

    @Id
    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "last_tag")
    private Long lastTag;

    public void increaseLastTag() {
        this.lastTag += 1;
    }

    public Long currentLastTag() {
        return this.lastTag;
    }

    public NickNameTag toNickNameTag() {
        return new NickNameTag(new NickName(nickName), lastTag);
    }

    @PrePersist
    private void onPrePersist() {
        if (this.lastTag == null) this.lastTag = 1L;
    }

}
