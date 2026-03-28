package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.CensorResult;
import com.bindglam.agora.utils.Requests;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface PostRequests {
    @Builder
    record Create(@NonNull String title, @NonNull String content, @NonNull String boardId, @NonNull String authorUsername) {
        @Builder
        public record Response(@Nullable PostDTO post, @Nullable CensorResult censorResult, @Nullable String errorMessage) implements Requests.Response {
            @Override
            public @Nullable String errorMessage() {
                return errorMessage != null ? errorMessage : (censorResult == null ? null : (censorResult.isGood() ? null : censorResult.getReason()));
            }
        }
    }

    @Builder
    record Comment(long postId, @NonNull String content, @NonNull String authorUsername) {
        @Builder
        public record Response(@Nullable CommentDTO comment, @Nullable CensorResult censorResult, @Nullable String errorMessage) implements Requests.Response {
            @Override
            public @Nullable String errorMessage() {
                return errorMessage != null ? errorMessage : (censorResult == null ? null : (censorResult.isGood() ? null : censorResult.getReason()));
            }
        }
    }
}
