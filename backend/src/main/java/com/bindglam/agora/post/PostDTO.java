package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.UserDTO;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public sealed interface PostDTO extends Censorable permits DefaultPostDTO, SecurePostDTO {
    @NonNull Long postId();

    @NonNull String title();

    @NonNull String content();

    @NonNull String boardId();

    @NonNull LocalDateTime createdAt();

    @NonNull UserDTO author();

    @NonNull PostVisibility visibility();

    @NonNull List<? extends CommentDTO> comments();
}
