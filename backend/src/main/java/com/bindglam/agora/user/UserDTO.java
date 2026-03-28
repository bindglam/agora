package com.bindglam.agora.user;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Builder
public record UserDTO(
        String username,
        UserPermission permission,
        LocalDateTime createdAt
) {

    public @NonNull User toEntity() {
        return User.builder()
                .username(username)
                .permission(permission)
                .createdAt(createdAt)
                .build();
    }
}
