package com.example.user.app.application.nickname.repository;

import com.example.user.app.application.nickname.entity.NickNameStorage;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NickNameHistoryRepository extends JpaRepository<NickNameStorage, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT n FROM NickNameStorage n WHERE n.nickName = :nickName")
    Optional<NickNameStorage> findByNickNameForUpdate(@Param("nickName") String nickName);

}
