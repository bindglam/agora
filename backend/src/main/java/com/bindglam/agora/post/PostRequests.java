package com.bindglam.agora.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface PostRequests {
    @RequiredArgsConstructor
    @Getter
    final class Create {
        private final String title;
        private final String content;
        private final String boardId;
        private final String author;
    }
}
