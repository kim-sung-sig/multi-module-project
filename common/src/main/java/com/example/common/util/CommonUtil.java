package com.example.common.util;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class CommonUtil {

    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof String str)
            return !StringUtils.hasText(str.trim());

        return ObjectUtils.isEmpty(obj);
    }

    public static boolean hasEmpty(Object... objs) {
        return Arrays.stream(objs).anyMatch(CommonUtil::isEmpty);
    }

    public static boolean hasBlank(String str) {
        return StringUtils.containsWhitespace(str);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String normalize(String str) {
        return trim(str);  // 확장 포인트
    }

}
