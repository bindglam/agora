package com.bindglam.agora.post.service;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.DefaultPostDTO;
import com.bindglam.agora.post.PostRequests;
import com.bindglam.agora.post.SecurePostDTO;
import com.bindglam.agora.user.UserDTO;
import org.hibernate.Internal;
import org.jspecify.annotations.NonNull;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface PostService {
    @Internal
    Optional<DefaultPostDTO> getUnsafePost(@NonNull UserDTO user, long postId);

    Optional<SecurePostDTO> getPost(@NonNull UserDTO user, long postId);

    Streamable<SecurePostDTO> getPostsByBoardId(@NonNull UserDTO user, @NonNull Board board, int page, int size);

    int getTotalPosts(@NonNull UserDTO user, @NonNull Board board);

    PostRequests.Create.@NonNull Response createPost(PostRequests.@NonNull Create request);

    PostRequests.Comment.@NonNull Response createComment(PostRequests.@NonNull Comment request);
}
