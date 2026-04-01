package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.post.repository.CommentRepository;
import com.bindglam.agora.post.repository.PostRepository;
import com.bindglam.agora.user.SecureUserDTO;
import com.bindglam.agora.user.repository.UserRepository;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Builder
public record SecurePostDTO(
        Long postId,
        String title,
        String content,
        String boardId,
        LocalDateTime createdAt,
        SecureUserDTO author,
        PostVisibility visibility,
        List<SecureCommentDTO> comments
) implements PostDTO<SecureCommentDTO>, Censorable {

    public Post toEntity(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
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
}
