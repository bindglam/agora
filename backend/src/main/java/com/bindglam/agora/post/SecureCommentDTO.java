package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.DefaultUserDTO;
import com.bindglam.agora.user.SecureUserDTO;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Builder
public record SecureCommentDTO(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long postId,
        SecureUserDTO author
) implements CommentDTO, Censorable {
    @Override
    public @Nullable String title() {
        return null;
    }
}
