package com.bindglam.agora.post.censor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum CensorServerType {
    LOCAL(LocalCensorServer::new),
    GEMINI(GeminiServer::new);

    private final Supplier<@NonNull CensorServer> supplier;

    public @NonNull CensorServer create() {
        return supplier.get();
    }
}
