package com.bindglam.agora.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jspecify.annotations.Nullable;

public interface Requests {
    interface Response {
        @Nullable String errorMessage();

        @JsonIgnore
        default boolean isSuccess() {
            return errorMessage() == null;
        }

        @JsonIgnore
        default boolean isFailure() {
            return errorMessage() != null;
        }
    }
}
