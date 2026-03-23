package com.bindglam.agora.post.service;

import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRequests;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostService {
    Optional<PostDTO> getPost(long postId);

    Page<PostDTO> getPostsByBoardId(String boardId, int page, int size);

    int getTotalPosts(String boardId);

    PostRequests.Create.@NonNull Response createPost(PostRequests.@NonNull Create request);
}
