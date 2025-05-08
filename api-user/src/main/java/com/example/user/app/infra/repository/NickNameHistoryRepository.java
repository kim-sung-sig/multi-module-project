package com.example.user.app.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.user.app.infra.entity.NickNameStorage;

import jakarta.persistence.LockModeType;

@Repository
public interface NickNameHistoryRepository extends JpaRepository<NickNameStorage, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT n FROM NickNameHistory n WHERE n.nickName = :nickName")
    Optional<NickNameStorage> findByNickNameForUpdate(@Param("nickName") String nickName);

}
