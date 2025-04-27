package com.example.common.domain.audit;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuditingEntityListener {

    @NonNull
    private final AuditorProvider auditorProvider;

    @PrePersist
    public void setCreatedInfo(Object target) {
        if (target instanceof AuditableEntity auditable) {
            AuditEntity audit = AuditEntity.builder()
                    .createdAt(LocalDateTime.now())
                    .createdBy(this.getCurrentAuditor())
                    .build();

            auditable.setAudit(audit);
        }
    }

    @PreUpdate
    public void setUpdatedInfo(Object target) {
        if (target instanceof AuditableEntity auditable) {
            AuditEntity audit = auditable.getAudit();

            if (Objects.isNull(audit)) {
                audit = new AuditEntity();
                audit.setCreatedBy(this.getCurrentAuditor());
                audit.setCreatedAt(LocalDateTime.now());
            }

            audit.setUpdatedAt(LocalDateTime.now());
            audit.setUpdatedBy(getCurrentAuditor());

            auditable.setAudit(audit);
        }
    }

    private String getCurrentAuditor() {
        return auditorProvider.getCurrentAuditor();
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // if (CommonUtil.isEmpty(auth) || !auth.isAuthenticated()) return "SYSTEM";
        // return auth.getName();
    }

}
