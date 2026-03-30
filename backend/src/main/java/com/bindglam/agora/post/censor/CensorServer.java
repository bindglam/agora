package com.bindglam.agora.post.censor;

import java.util.concurrent.CompletableFuture;

public interface CensorServer {
    CompletableFuture<CensorResult> tryCensor(Censorable target);
}
