package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.DefaultUserDTO;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public record DefaultPostDTO(
        Long postId,
        String title,
        String content,
        String boardId,
        LocalDateTime createdAt,
        DefaultUserDTO author,
        PostVisibility visibility,
        List<DefaultCommentDTO> comments
) implements PostDTO, Censorable {
    public @NonNull Post toEntity() {
        return Post.builder()
                .id(postId)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author.toEntity())
                .visibility(visibility)
                .comments(new ArrayList<>(comments.stream().map(DefaultCommentDTO::toEntity).toList()))
                .build();
    }

    public @NonNull SecurePostDTO secure() {
        return SecurePostDTO.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author.secure())
                .visibility(visibility)
                .comments(comments.stream().map(DefaultCommentDTO::secure).toList())
                .build();
    }
}
