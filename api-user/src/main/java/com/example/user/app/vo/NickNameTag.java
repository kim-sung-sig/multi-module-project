package com.example.user.app.vo;

import com.example.common.util.CommonUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class NickNameTag {

    private final NickName nickName;
    private final Long tag;

    public NickNameTag(NickName nickName, Long tag) {
        validateFormat(nickName, tag);
        this.nickName = nickName;
        this.tag = tag;
    }

    public String displayNickNameTag() {
        return nickName.get() + "_" + tag;
    }

    private void validateFormat(NickName nickName, Long tag) {
        if (CommonUtil.hasEmpty(nickName, tag)) throw new IllegalArgumentException("NickNameTag can not");
    }

}
