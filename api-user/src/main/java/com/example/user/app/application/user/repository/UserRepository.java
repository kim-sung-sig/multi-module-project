package com.example.user.app.application.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.user.app.application.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {

    Optional<User> findByUsername(@NonNull String username);

    boolean existsByUsername(@NonNull String username);

    boolean existsById(@NonNull UUID id);

}
