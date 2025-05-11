package com.example.user.app.application.auth.components;

import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import com.example.user.app.common.config.security.AccessTokenBlackListProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AccessTokenBlackListProviderImpl implements AccessTokenBlackListProvider {

    private final Map<String, Date> blackList = new ConcurrentHashMap<>();

    /**
     * 블랙리스트 추가 
     */
    @Override
    public void add(String accessToken) {
        Date expiration = JwtUtil.getExpiration(accessToken);
        if (CommonUtil.isEmpty(expiration) || expiration.before(new Date())) {
            log.warn("만료된 토큰은 등록하지 않음. token={}", accessToken);
            return;
        }

        blackList.put(accessToken, expiration);
        log.debug("블랙리스트 등록: token={}, expireAt={}", accessToken, expiration);
    }

    /**
     * 블랙리스트인지 확인
     */
    @Override
    public boolean isBlack(String accessToken) {
        var expiry = blackList.get(accessToken);
        return !CommonUtil.isEmpty(expiry);
    }

    /**
     * 블랙리스트 제거 스케줄러 -> TODO 추후 redis 와 같은 TTL 로 처리
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void cleanExpired() {
        int before = blackList.size();
        int removedCount = 0;

        for (Map.Entry<String, Date> entry : blackList.entrySet()) {
            if (entry.getValue().before(new Date())) {
                blackList.remove(entry.getKey());
                removedCount++;
            }
        }

        log.info("블랙리스트 정리 완료: before={}, removed={}, after={}",
                before, removedCount, blackList.size());
    }

}
