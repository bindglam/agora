package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.DefaultUserDTO;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Builder
public record DefaultCommentDTO(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long postId,
        DefaultUserDTO author
) implements CommentDTO, Censorable {
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

    public @NonNull SecureCommentDTO secure() {
        return SecureCommentDTO.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .postId(postId)
                .author(author.secure())
                .build();
    }
}
