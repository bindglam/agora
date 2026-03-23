package com.bindglam.agora.post;

import com.bindglam.agora.user.User;
import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.utils.Identified;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Builder
public record PostDTO(
        Long postId,
        String title,
        String content,
        String boardId,
        LocalDateTime createdAt,
        UserDTO author,
        boolean isPublic
) implements Identified {
    @Override
    public @NonNull String id() {
        return String.valueOf(postId);
    }

    public Post toEntity() {
        return Post.builder()
                .id(postId)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author.toEntity())
                .isPublic(isPublic)
                .build();
    }
}
