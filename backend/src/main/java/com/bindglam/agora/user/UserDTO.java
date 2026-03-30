package com.bindglam.agora.user;

import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public sealed interface UserDTO permits DefaultUserDTO, SecureUserDTO {
    @NonNull String username();

    @NonNull UserPermission permission();

    @NonNull LocalDateTime createdAt();
}
