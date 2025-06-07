package com.example.user.app.application.user.service;

import com.example.user.app.application.nickname.domain.NickName;

import java.util.UUID;

public interface UserSearchService {

    Object getUserByNickname(NickName nickName);

    Object getUserWithProfileByNickname(NickName nickName);

    Object getUserDetailById(UUID id);

    Object getUserWithDetailAndProfileById(UUID id);
}
