package com.example.common.util;

import com.example.common.interfaces.ThrowingRunable;
import org.springframework.util.function.ThrowingSupplier;

public final class TryUtil {

    public static <T> T get(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw asRuntimeException(e);
        }
    }

    public static void run(ThrowingRunable runable) {
        try {
            runable.run();
        } catch (Exception e) {
            throw asRuntimeException(e);
        }
    }

    private static RuntimeException asRuntimeException(Exception e) {
        return (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
    }

}
