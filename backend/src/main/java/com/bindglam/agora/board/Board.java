package com.bindglam.agora.board;

import lombok.Builder;

@Builder
public record Board(
        String id,
        BoardProperties properties
) {
}
