package com.example.user.app.application.service.nickname;

import org.springframework.stereotype.Service;

import com.example.user.app.infra.entity.NickNameStorage;
import com.example.user.app.infra.entity.User;
import com.example.user.app.infra.repository.NickNameHistoryRepository;
import com.example.user.app.vo.NickName;
import com.example.user.app.vo.NickNameTag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
