package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.post.repository.CommentRepository;
import com.bindglam.agora.post.repository.PostRepository;
import com.bindglam.agora.user.SecureUserDTO;
import com.bindglam.agora.user.repository.UserRepository;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Builder
public record SecureCommentDTO(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long postId,
        SecureUserDTO author
) implements CommentDTO<SecureUserDTO>, Censorable {
    @Override
    public @Nullable String title() {
        return null;
    }

    public Comment toEntity(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        Comment comment = commentRepository.findById(id).orElseGet(() ->
                Comment.builder()
                        .id(id)
                        .content(content)
                        .createdAt(createdAt)
                        .postId(postId)
                        .build()
        );

        if (comment.getAuthor() == null && author != null) {
            comment.setAuthor(author.toEntity(userRepository));
        }

        return comment;
    }
}
