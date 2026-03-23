package com.bindglam.agora.post;

import com.bindglam.agora.utils.Identified;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Builder
public record PostDTO(
        Long postId,
        String title,
        String content,
        String boardId,
        LocalDateTime createdAt,
        String author,
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
                .author(author)
                .isPublic(isPublic)
                .build();
    }
}
