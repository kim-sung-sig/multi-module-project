package com.example.user.app.infra.entity;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.common.enums.IsUsed;
import com.example.user.app.infra.entity.convertor.IsUsedConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    name = "dn_user_profile_picture",
    indexes = {
        @Index(name = "idx_user_profile_picture_user_id", columnList = "user_id"),
        @Index(name = "idx_user_profile_picture_status", columnList = "status"),})
@Getter @ToString @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
public class UserProfilePicture {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "status", nullable = false)
    @Convert(converter = IsUsedConverter.class)
    private IsUsed status;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_url")
    private String fileUrl;
}
