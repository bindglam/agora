package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.CensorResult;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

public interface PostRequests {
    @RequiredArgsConstructor
    @Getter
    final class Create {
        private final String title;
        private final String content;
        private final String boardId;
        private final String author;

        @RequiredArgsConstructor
        @Getter
        @Builder
        public static final class Response {
            private final @Nullable PostDTO post;
            private final CensorResult censorResult;
        }
    }
}
