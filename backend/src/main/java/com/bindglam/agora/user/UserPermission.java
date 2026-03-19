package com.bindglam.agora.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
@Getter
public enum UserPermission {
    DEFAULT(null),
    ADMIN(UserPermission.DEFAULT);

    private final @Nullable UserPermission parent;

    public boolean hasPermission(UserPermission other) {
        if(this == other) return true;
        return parent != null && parent.hasPermission(other);
    }
}
