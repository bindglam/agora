package com.bindglam.agora.user;

import com.bindglam.agora.utils.Identified;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Builder
public record UserDTO(
        String username,
        UserPermission permission,
        LocalDateTime createdAt
) implements Identified {
    @Override
    public @NonNull String id() {
        return username;
    }

    public @NonNull User toEntity() {
        return User.builder()
                .username(username)
                .permission(permission)
                .createdAt(createdAt)
                .build();
    }
}
