package com.bindglam.agora.user;

import com.bindglam.agora.utils.Requests;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface UserRequests {
    @Builder
    record Create(@NonNull String username) {
        @Builder
        public record Response(@Nullable UserDTO user, @Nullable String errorMessage) implements Requests.Response {
        }
    }
}
