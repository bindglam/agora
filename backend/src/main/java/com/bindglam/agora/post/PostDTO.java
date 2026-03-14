package com.bindglam.agora.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PostDTO implements Post {
    private Long id;
    private String title;
    private String content;
    private String boardId;
    private LocalDateTime createdAt;

    public PostEntity toEntity() {
        return PostEntity.builder()
                .id(id)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .build();
    }
}
