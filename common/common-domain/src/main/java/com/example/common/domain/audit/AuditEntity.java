package com.example.common.domain.audit;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AuditEntity {

    @Column(name = "created_at") @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "created_by") @CreatedBy
    private String createdBy;       // username 과 일치

    @Column(name = "updated_at") @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "updated_by") @LastModifiedBy
    private String updatedBy;       // username 과 일치

}
