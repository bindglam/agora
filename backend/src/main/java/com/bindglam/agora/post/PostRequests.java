package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.CensorResult;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

public interface PostRequests {
    record Create(String title, String content, String boardId, String author) {
        @Builder
        public record Response(@Nullable PostDTO post, CensorResult censorResult) {
        }
    }
}
