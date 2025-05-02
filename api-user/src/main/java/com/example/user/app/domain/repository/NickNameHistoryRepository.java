package com.example.user.app.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.user.app.domain.entity.NickNameHistory;

import jakarta.persistence.LockModeType;

@Repository
public interface NickNameHistoryRepository extends JpaRepository<NickNameHistory, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT n FROM NickNameHistory n WHERE n.nickName = :nickName")
    Optional<NickNameHistory> findByNickNameForUpdate(@Param("nickName") String nickName);

}
