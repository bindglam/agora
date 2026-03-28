package com.bindglam.agora.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class User {
    @Id
    @Column(columnDefinition = "NVARCHAR", length = 20)
    private String username;

    private UserPermission permission;

    private LocalDateTime createdAt;

    public @NonNull UserDTO toDTO() {
        return UserDTO.builder()
                .username(username)
                .permission(permission)
                .createdAt(createdAt)
                .build();
    }
}
