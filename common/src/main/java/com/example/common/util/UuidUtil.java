package com.example.common.util;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

/*
 * UUIDv7Generator
 */
public class UuidUtil {

    public static UUID generate() {
        return UuidCreator.getTimeOrdered(); // UUIDv7
    }

}
