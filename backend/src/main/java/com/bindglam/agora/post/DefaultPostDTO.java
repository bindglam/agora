package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.post.repository.CommentRepository;
import com.bindglam.agora.post.repository.PostRepository;
import com.bindglam.agora.user.DefaultUserDTO;
import com.bindglam.agora.user.repository.UserRepository;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
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
) implements PostDTO<DefaultCommentDTO>, Censorable {

    public @NonNull Post toEntity() {
        return Post.builder()
                .id(postId)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author.toEntity())
                .visibility(visibility)
                .comments(comments.stream().map(DefaultCommentDTO::toEntity).toList())
                .build();
    }

    public @NonNull Post toEntity(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        Post post = postRepository.findById(postId).orElseGet(() ->
                Post.builder()
                        .id(postId)
                        .title(title)
                        .content(content)
                        .boardId(boardId)
                        .createdAt(createdAt)
                        .visibility(visibility)
                        .build()
        );

        if (post.getAuthor() == null && author != null) {
            post.setAuthor(author.toEntity(userRepository));
        }

        post.setComments(comments.stream()
                .map(c -> c.toEntity(postRepository, userRepository, commentRepository))
                .toList());

        return post;
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
