package com.bindglam.agora.user;

import com.bindglam.agora.utils.Identified;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class UserDTO implements Identified {
    private String nickname;
    private String password;
    private UserPermission permission;

    @Override
    public @NonNull String id() {
        return nickname;
    }

    public @NonNull User toEntity() {
        return User.builder()
                .nickname(nickname)
                .password(password)
                .permission(permission)
                .build();
    }
}
