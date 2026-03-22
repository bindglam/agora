package com.bindglam.agora.post.censor;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Getter
public final class CensorResult {
    private final boolean isGood;
    private final @Nullable String reason;

    private CensorResult(boolean isGood, @Nullable String reason) {
        this.isGood = isGood;
        this.reason = reason;
    }

    public static @NonNull CensorResult good(@Nullable String reason) {
        return new CensorResult(true, reason);
    }

    public static @NonNull CensorResult bad(@Nullable String reason) {
        return new CensorResult(false, reason);
    }
}
