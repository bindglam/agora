package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.UserDTO;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Builder
public record CommentDTO(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long postId,
        UserDTO author
) implements Censorable {
    @Override
    public @Nullable String title() {
        return null;
    }

    public @NonNull Comment toEntity() {
        return Comment.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .postId(postId)
                .author(author.toEntity())
                .build();
    }
}
