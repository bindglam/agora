package com.bindglam.agora.registry;

import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface Registry<T> {
    Optional<T> get(String key);

    default @NonNull T getOrThrow(String key) {
        return get(key).orElseThrow();
    }
}
