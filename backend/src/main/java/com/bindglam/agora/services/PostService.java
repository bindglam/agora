package com.bindglam.agora.services;

import com.bindglam.agora.post.Post;

import java.util.Optional;

public interface PostService {
    Optional<Post> getPost(long postId);
}
