package com.bindglam.agora.service;

import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRequests;
import com.bindglam.agora.post.censor.CensorResult;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostService {
    Optional<PostDTO> getPost(long postId);

    Page<PostDTO> getPostsByBoardId(String boardId, int page, int size);

    PostRequests.Create.Response createPost(PostDTO post);
}
