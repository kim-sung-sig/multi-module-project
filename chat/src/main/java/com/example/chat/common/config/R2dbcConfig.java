package com.example.chat.common.config;

import com.example.chat.infra.entity.Auditable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Configuration
public class R2dbcConfig {

    @Bean
    public BeforeSaveCallback<Auditable> r2dbcAuditingCallback() {
        return (entity, outbox, table) -> {
            if (entity.getId() == null) {
                entity.setCreatedAt(LocalDateTime.now());
            }
            // 모든 저장 시 updatedAt을 설정
            entity.setUpdatedAt(LocalDateTime.now());

            return Mono.just(entity);
        };
    }
}
