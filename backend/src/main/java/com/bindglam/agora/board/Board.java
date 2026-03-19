package com.bindglam.agora.board;

import com.bindglam.agora.utils.Identified;
import lombok.Builder;

@Builder
public record Board(
        String id,
        BoardProperties properties
) implements Identified {
}
