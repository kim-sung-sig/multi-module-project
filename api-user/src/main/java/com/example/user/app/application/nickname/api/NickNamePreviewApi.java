package com.example.user.app.application.nickname.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.model.ApiResponse;
import com.example.user.app.application.nickname.domain.NickName;
import com.example.user.app.application.nickname.domain.NickNameTag;
import com.example.user.app.application.nickname.service.NickNameTagPreviewService;
import com.example.user.app.common.dto.security.SecurityUser;
import com.example.user.app.common.util.ApiResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/v1/nickname-preview")
@RequiredArgsConstructor
public class NickNamePreviewApi {

    private final NickNameTagPreviewService nickNameTagPreviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<NickNameTag>> preview(
        @AuthenticationPrincipal SecurityUser securityUser,
        @RequestParam("nickName") String nickName
    ) {
        return ApiResponseUtil.ok(
            nickNameTagPreviewService.previewTag(securityUser.getId(), new NickName(nickName)));
    }
    
}
