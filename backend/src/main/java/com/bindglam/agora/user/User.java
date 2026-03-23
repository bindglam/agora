package com.bindglam.agora.user;

import com.bindglam.agora.utils.Identified;
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
public final class User implements Identified {
    @Id
    @Column(columnDefinition = "TEXT", length = 20)
    private String username;

    private UserPermission permission;

    private LocalDateTime createdAt;

    @Override
    public @NonNull String id() {
        return username;
    }

    public @NonNull UserDTO toDTO() {
        return UserDTO.builder()
                .username(username)
                .permission(permission)
                .createdAt(createdAt)
                .build();
    }
}
