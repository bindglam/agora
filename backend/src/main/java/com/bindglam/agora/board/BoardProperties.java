package com.bindglam.agora.board;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public final class BoardProperties {
    private final String displayName;
    private final String description;
    @Builder.Default
    private final boolean isPostable = true;
}
