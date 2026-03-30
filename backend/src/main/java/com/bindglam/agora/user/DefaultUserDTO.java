package com.bindglam.agora.user;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DefaultUserDTO(
        UUID key,
        String username,
        UserPermission permission,
        LocalDateTime createdAt
) implements UserDTO {

    public @NonNull User toEntity() {
        return User.builder()
                .key(key)
                .username(username)
                .permission(permission)
                .createdAt(createdAt)
                .build();
    }

    public @NonNull SecureUserDTO secure() {
        return SecureUserDTO.builder()
                .username(username)
                .permission(permission)
                .createdAt(createdAt)
                .build();
    }
}
