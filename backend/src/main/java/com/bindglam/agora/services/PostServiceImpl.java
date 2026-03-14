package com.bindglam.agora.services;

import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public PostDTO getPost(long postId) {
        return postRepository.getReferenceById(postId).toDTO();
    }
}
