package com.bindglam.agora.user;

import com.bindglam.agora.utils.Identified;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.jspecify.annotations.NonNull;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class User implements Identified {
    @Id
    private String nickname;

    private String password;

    @Override
    public @NonNull String id() {
        return nickname;
    }

    public @NonNull UserDTO toDTO() {
        return UserDTO.builder()
                .nickname(nickname)
                .password(password)
                .build();
    }
}
