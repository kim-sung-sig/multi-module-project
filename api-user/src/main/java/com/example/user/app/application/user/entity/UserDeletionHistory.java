package com.example.user.app.application.user.entity;

import com.example.common.util.GsonProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_deletion_history")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
public class UserDeletionHistory {

    @Id
    private UUID id; // UUID of the user

    @Column(name = "user_info")
    private String userInfo;

    @CreatedDate
    @Column(name = "deleted_at")
    @Comment("삭제 일시")
    private Instant deletedAt;

    @CreatedBy
    @Column(name = "deleted_by")
    @Comment("삭제자")
    private String deletedBy;

    @Column(name = "deleted_reason")
    @Comment("삭제 사유")
    private String deletedReason;

    public UserDeletionHistory(User user, String deletedReason) {
        this.id = user.getId();
        this.userInfo = GsonProvider.toJson(user);
        this.deletedReason = deletedReason;
    }

}
