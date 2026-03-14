package com.bindglam.agora.service;

import com.bindglam.agora.post.PostDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<PostDTO> getPost(long postId);

    List<PostDTO> getPostsByBoardId(String boardId);
}
