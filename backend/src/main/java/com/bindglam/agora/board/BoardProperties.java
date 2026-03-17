package com.bindglam.agora.board;

import lombok.Builder;

@Builder
public record BoardProperties(
        String displayName,
        String description
) {
}
