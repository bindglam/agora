package com.bindglam.agora.utils;

import org.jspecify.annotations.Nullable;

public interface Requests {
    interface Response {
        @Nullable String errorMessage();

        default boolean isSuccess() {
            return errorMessage() == null;
        }

        default boolean isFailure() {
            return errorMessage() != null;
        }
    }
}
