package com.bindglam.agora.post.censor;

import com.bindglam.agora.post.censor.server.GeminiServer;
import com.bindglam.agora.post.censor.server.OllamaServer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum CensorServerType {
    OLLAMA(OllamaServer::new),
    GEMINI(GeminiServer::new);

    private final Supplier<@NonNull CensorServer> supplier;

    public @NonNull CensorServer create() {
        return supplier.get();
    }
}
