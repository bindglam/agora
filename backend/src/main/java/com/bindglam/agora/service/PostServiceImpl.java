package com.bindglam.agora.service;

import com.bindglam.agora.post.Post;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Optional<PostDTO> getPost(long postId) {
        return postRepository.findById(postId).map(Post::toDTO);
    }

    @Override
    public List<PostDTO> getPostsByBoardId(String boardId) {
        return postRepository.findByBoardId(boardId).stream().map(Post::toDTO).toList();
    }

    @Override
    public PostDTO savePost(PostDTO post) {
        return postRepository.save(post.toEntity()).toDTO();
    }
}
