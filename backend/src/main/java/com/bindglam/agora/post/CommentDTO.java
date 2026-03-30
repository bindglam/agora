package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.UserDTO;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public sealed interface CommentDTO extends Censorable permits DefaultCommentDTO, SecureCommentDTO {
    @NonNull Long id();

    @NonNull String content();

    @NonNull LocalDateTime createdAt();

    @NonNull Long postId();

    @NonNull UserDTO author();
}
