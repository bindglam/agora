package com.bindglam.agora.user;

import com.bindglam.agora.user.repository.UserRepository;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record SecureUserDTO(
        @NonNull String username,
        @NonNull LocalDateTime createdAt
) implements UserDTO {

    public User toEntity(UserRepository userRepository) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Can't create a new User without key and permission, so return a placeholder
        // This is expected to be used only when an entity already exists
        throw new IllegalStateException("Cannot create new User from SecureUserDTO - entity not found for username: " + username);
    }
}
