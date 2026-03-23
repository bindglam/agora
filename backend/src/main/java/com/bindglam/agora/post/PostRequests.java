package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.CensorResult;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface PostRequests {
    @Builder
    record Create(@NonNull String title, @NonNull String content, @NonNull String boardId, @NonNull String author) {
        @Builder
        public record Response(@Nullable PostDTO post, @NonNull CensorResult censorResult) {
        }
    }
}
