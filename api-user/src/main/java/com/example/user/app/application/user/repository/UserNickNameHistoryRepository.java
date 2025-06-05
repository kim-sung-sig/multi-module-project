package com.example.user.app.application.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.app.application.user.entity.UserNickNameHistory;

@Repository
public interface UserNickNameHistoryRepository extends JpaRepository<UserNickNameHistory, Long> {

    Optional<UserNickNameHistory> findByUserIdAndNickName(UUID userId, String nickName);

}
