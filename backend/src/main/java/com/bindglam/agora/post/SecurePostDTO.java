package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.SecureUserDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

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
) implements PostDTO, Censorable {
}
