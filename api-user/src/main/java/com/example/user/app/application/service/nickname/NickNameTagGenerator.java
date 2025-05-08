package com.example.user.app.application.service.nickname;

import org.springframework.stereotype.Component;

import com.example.user.app.infra.entity.NickNameStorage;
import com.example.user.app.infra.repository.NickNameHistoryRepository;
import com.example.user.app.vo.NickName;
import com.example.user.app.vo.NickNameTag;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NickNameTagGenerator {

    private final NickNameHistoryRepository nickNameRepository;

    /**
     * 주어진 기본 닉네임에 대해 고유한 태그를 생성합니다.
     * 태그는 기본 닉네임에 증가된 순번을 추가하여 생성됩니다.
     *
     * @param baseNickName 기본 닉네임
     * @return 생성된 고유 태그
     */
    public NickNameTag previewTag(NickName nickName) {
        NickNameStorage nickNameStorage = nickNameRepository.findById(nickName.get())
                .orElseGet(() -> new NickNameStorage(nickName.get(), 0L));

        nickNameStorage.increaseLastTag();

        return nickNameStorage.toNickNameTag();
    }

    /**
     * 주어진 기본 닉네임에 대해 고유한 태그를 생성합니다.
     * 태그는 기본 닉네임에 증가된 순번을 추가하여 생성됩니다.
     * 
     * @param baseNickName 기본 닉네임
     * @return 생성된 고유 태그
     */
    @Transactional
    public NickNameTag generateTag(NickName nickName) {
        NickNameStorage nickNameStorage = nickNameRepository.findByNickNameForUpdate(nickName.get())
                .orElseGet(() -> new NickNameStorage(nickName.get(), 0L));

        nickNameStorage.increaseLastTag();

        nickNameRepository.save(nickNameStorage);

        return nickNameStorage.toNickNameTag();
    }

}
