package com.bindglam.agora.user;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public enum UserPermission {
    DEFAULT(null),
    ADMIN(DEFAULT);

    private final @Nullable UserPermission parent;

    public boolean check(@NonNull UserPermission required) {
        if(required.equals(this))
            return true;
        if(parent == null)
            return false;
        return parent.check(required);
    }
}
