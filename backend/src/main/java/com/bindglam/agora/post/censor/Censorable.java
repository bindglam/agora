package com.bindglam.agora.post.censor;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface Censorable {
    @Nullable String title();

    @NonNull String content();
}
