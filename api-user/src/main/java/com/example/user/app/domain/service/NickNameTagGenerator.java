package com.example.user.app.domain.service;

import org.springframework.stereotype.Component;

import com.example.user.app.domain.entity.NickNameHistory;
import com.example.user.app.domain.repository.NickNameHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NickNameTagGenerator {

    private final NickNameHistoryRepository nickNameHistoryRepository;

    /**
     * 주어진 기본 닉네임에 대해 고유한 태그를 생성합니다.
     * 태그는 기본 닉네임에 증가된 순번을 추가하여 생성됩니다.
     *
     * @param baseNickName 기본 닉네임
     * @return 생성된 고유 태그
     */
    public String previewTag(String baseNickName) {
        NickNameHistory nickNameHistory = nickNameHistoryRepository.findById(baseNickName)
                .orElse(NickNameHistory.builder()
                        .nickName(baseNickName)
                        .seq(0L)
                        .build());

        long nextSeq = nickNameHistory.getSeq() + 1; // 다음 순번 미리보기
        return baseNickName + "_" + nextSeq;
    }

    /**
     * 주어진 기본 닉네임에 대해 고유한 태그를 생성합니다.
     * 태그는 기본 닉네임에 증가된 순번을 추가하여 생성됩니다.
     * 
     * @param baseNickName 기본 닉네임
     * @return 생성된 고유 태그
     */
    @Transactional
    public String generateTag(String baseNickName) {
        NickNameHistory nickNameHistory = nickNameHistoryRepository.findByNickNameForUpdate(baseNickName)
                .orElseGet(() -> {
                    return NickNameHistory.builder()
                            .nickName(baseNickName)
                            .seq(0L)
                            .build();
                });

        long newSeq = incrementSeqAndGet(nickNameHistory);
        nickNameHistoryRepository.save(nickNameHistory);

        return baseNickName + "_" + newSeq;
    }

    private Long incrementSeqAndGet(NickNameHistory nickNameHistory) {
        nickNameHistory.incrementSeq();
        return nickNameHistory.getSeq();
    }
}
