package com.bindglam.agora.post.service;

import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRequests;
import com.bindglam.agora.user.UserDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface PostService {
    Optional<PostDTO> getPost(@NonNull UserDTO user, long postId);

    Streamable<PostDTO> getPostsByBoardId(@NonNull UserDTO user, String boardId, int page, int size);

    int getTotalPosts(String boardId);

    PostRequests.Create.@NonNull Response createPost(PostRequests.@NonNull Create request);
}
