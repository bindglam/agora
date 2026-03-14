package com.bindglam.agora.service;

import com.bindglam.agora.post.PostDTO;

import java.util.Optional;

public interface PostService {
    Optional<PostDTO> getPost(long postId);
}
