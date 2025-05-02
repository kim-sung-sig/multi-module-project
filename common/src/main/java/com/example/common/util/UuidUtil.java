package com.example.common.util;

import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

/*
 * UUIDv7Generator
 */
public class UuidUtil {

    public static UUID generate() {
        return UuidCreator.getTimeOrdered(); // UUIDv7
    }

}
