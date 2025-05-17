package com.example.user.app.common.config.jpa;

import com.example.common.model.SecurityUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    AuditorAware<UUID> getCurrentAuditor() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                return Optional.empty();
            }

            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return Optional.of(securityUser.getId()); // UUID
        };
    }

}
