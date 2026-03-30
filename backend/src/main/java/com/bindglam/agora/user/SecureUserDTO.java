package com.bindglam.agora.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SecureUserDTO(
        String username,
        UserPermission permission,
        LocalDateTime createdAt
) implements UserDTO {
}
