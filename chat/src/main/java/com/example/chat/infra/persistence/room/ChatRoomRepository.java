package com.example.chat.infra.persistence.room;

import com.example.chat.infra.entity.room.ChatRoomEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatRoomRepository extends R2dbcRepository<ChatRoomEntity, Long> {

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM user_chat_room ucr
            WHERE
                ucr.chat_room_id = :chatRoomId
                AND ucr.user_id = :userId
        )
    """)
    Mono<Boolean> isMember(Long chatRoomId, Long userId);

}
