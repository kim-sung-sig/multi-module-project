package com.example.chat.core.infra.persistence.message;

import com.example.chat.core.infra.entity.message.TextMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextMessageJpaRepository extends JpaRepository<TextMessageEntity, Long> {
}
