package com.example.user.app.application.nickname.service;

import com.example.user.app.application.nickname.domain.NickName;
import com.example.user.app.application.nickname.domain.NickNameTag;
import com.example.user.app.application.nickname.entity.NickNameStorage;
import com.example.user.app.application.nickname.repository.NickNameHistoryRepository;
import com.example.user.app.application.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NickNameTagPreviewService {

    private final NickNameHistoryRepository nickNameRepository;

    public NickNameTag previewTag(User user, String baseNickName) {
        NickNameStorage nickName = nickNameRepository.findById(baseNickName)
                .orElse(new NickNameStorage(baseNickName, 0L));

        Long nextTag = nickName.getLastTag() + 1;
        return new NickNameTag(new NickName(baseNickName), nextTag);
    }

}
