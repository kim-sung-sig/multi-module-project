package com.example.user.app.application.nickname.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.user.app.application.nickname.domain.NickName;
import com.example.user.app.application.nickname.domain.NickNameTag;
import com.example.user.app.application.nickname.entity.NickNameStorage;
import com.example.user.app.application.nickname.repository.NickNameHistoryRepository;
import com.example.user.app.application.user.entity.UserNickNameHistory;
import com.example.user.app.application.user.repository.UserNickNameHistoryRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NickNameTagPreviewService {

    private final NickNameHistoryRepository nickNameRepository;
    private final UserNickNameHistoryRepository userNickNameHistoryRepository;

    public NickNameTag previewTag(@NotNull UUID requestUserId, @NotNull NickName requestNickName) {

        Optional<UserNickNameHistory> historyOpt = userNickNameHistoryRepository.findByUserIdAndNickName(requestUserId, requestNickName.get());
        if (historyOpt.isPresent()) {
            log.info("User {} already has a nickname: {}", requestUserId, requestNickName.get());
            val history = historyOpt.get();
            return new NickNameTag(new NickName(history.getNickName()), history.getTag());
        }

        NickNameStorage nickName = nickNameRepository.findById(requestNickName.get())
                .orElse(new NickNameStorage(requestNickName.get(), 0L));

        Long nextTag = nickName.getLastTag() + 1;
        return new NickNameTag(requestNickName, nextTag);
    }

}
