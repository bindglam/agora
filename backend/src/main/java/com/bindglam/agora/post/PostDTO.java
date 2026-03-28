package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.UserDTO;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public record PostDTO(
        Long postId,
        String title,
        String content,
        String boardId,
        LocalDateTime createdAt,
        UserDTO author,
        PostVisibility visibility,
        List<CommentDTO> comments
) implements Censorable {
    public @NonNull Post toEntity() {
        return Post.builder()
                .id(postId)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author.toEntity())
                .visibility(visibility)
                .comments(new ArrayList<>(comments.stream().map(CommentDTO::toEntity).toList()))
                .build();
    }
}
