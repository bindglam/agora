package com.bindglam.agora.board;

import com.bindglam.agora.user.UserPermission;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@RequiredArgsConstructor
@Getter
public final class BoardProperties {
    private final String displayName;
    private final String description;
    @Builder.Default
    private final Permissions permissions = Permissions.builder().build();


    @Builder
    @RequiredArgsConstructor
    @Getter
    public static final class Permissions {
        @Builder.Default
        @Accessors(fluent = true)
        private final UserPermission posting = UserPermission.DEFAULT;
    }
}
