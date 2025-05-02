package com.example.common.interfaces;

@FunctionalInterface
public interface ThrowingPredicate<T> {
    boolean test(T t) throws Exception;

    default ThrowingPredicate<T> and(ThrowingPredicate<? super T> other) {
        return (T t) -> test(t) && other.test(t);
    }

    default ThrowingPredicate<T> or(ThrowingPredicate<? super T> other) {
        return (T t) -> test(t) || other.test(t);
    }
}
