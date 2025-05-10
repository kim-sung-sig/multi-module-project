package com.example.user.app.application.nickname;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

@Repository
public interface NickNameHistoryRepository extends JpaRepository<NickNameStorage, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT n FROM NickNameHistory n WHERE n.nickName = :nickName")
    Optional<NickNameStorage> findByNickNameForUpdate(@Param("nickName") String nickName);

}
