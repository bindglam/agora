package com.bindglam.agora.services;

import com.bindglam.agora.post.Post;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Optional<PostDTO> getPost(long postId) {
        return postRepository.findById(postId).map(Post::toDTO);
    }
}
