package com.example.chat.infra.entity;

import java.time.LocalDateTime;

public interface Auditable {
    Long getId();
    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
}
