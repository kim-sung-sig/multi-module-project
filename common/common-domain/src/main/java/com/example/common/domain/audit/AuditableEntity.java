package com.example.common.domain.audit;

/*
 * AuditableEntity.java
 * spring audiatable 을 대체하기 위한 인터페이스
 */
public interface AuditableEntity {

    AuditEntity getAudit();

    void setAudit(AuditEntity audit);

}
