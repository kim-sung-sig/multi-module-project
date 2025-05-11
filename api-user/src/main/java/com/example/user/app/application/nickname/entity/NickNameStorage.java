package com.example.user.app.application.nickname.entity;

import com.example.user.app.application.nickname.domain.NickName;
import com.example.user.app.application.nickname.domain.NickNameTag;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
