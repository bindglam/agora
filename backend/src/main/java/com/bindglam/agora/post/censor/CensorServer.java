package com.bindglam.agora.post.censor;

import com.bindglam.agora.post.PostDTO;

import java.util.concurrent.CompletableFuture;

public interface CensorServer {
    CompletableFuture<CensorResult> tryCensor(PostDTO post);
}
