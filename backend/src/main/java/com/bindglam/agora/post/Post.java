package com.bindglam.agora.post;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.utils.Identified;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public interface Post extends Identified {
    @NonNull Long getId();

    @Override
    default @NonNull String id() {
        return String.valueOf(getId());
    }

    @NonNull String getTitle();

    @NonNull String getContent();

    @NonNull String getBoardId();

    default @NonNull Board getBoard() {
        return BuiltInRegistries.BOARDS.getOrThrow(getBoardId());
    }

    @NonNull LocalDateTime getCreatedAt();
}
