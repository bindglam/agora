package com.bindglam.agora.post;

import com.bindglam.agora.utils.Identified;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PostDTO implements Identified {
    private Long id;
    private String title;
    private String content;
    private String boardId;
    private LocalDateTime createdAt;
    private String author;
    private boolean isPublic;

    @Override
    public @NonNull String id() {
        return String.valueOf(id);
    }

    public Post toEntity() {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author)
                .isPublic(isPublic)
                .build();
    }
}
