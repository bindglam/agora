package com.bindglam.agora.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class User {
    @Id
    @Column(name = "key", columnDefinition = "BINARY(16)",  updatable = false, nullable = false)
    @Builder.Default
    private UUID key = UUID.randomUUID();

    @Column(columnDefinition = "NVARCHAR", length = 20)
    private String username;

    private UserPermission permission;

    private LocalDateTime createdAt;

    public @NonNull DefaultUserDTO toDTO() {
        return DefaultUserDTO.builder()
                .key(key)
                .username(username)
                .permission(permission)
                .createdAt(createdAt)
                .build();
    }
}
