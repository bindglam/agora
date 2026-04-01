package com.bindglam.agora.user;

import com.bindglam.agora.user.repository.UserRepository;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.Optional;
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

    public User toEntity(UserRepository userRepository) {
        if (key != null) {
            Optional<User> existing = userRepository.findById(key);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        // Fall back to creating a new entity
        return toEntity();
    }

    public @NonNull SecureUserDTO secure() {
        return SecureUserDTO.builder()
                .username(username)
                .createdAt(createdAt)
                .build();
    }
}
