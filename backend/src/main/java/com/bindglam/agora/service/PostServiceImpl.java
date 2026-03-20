package com.bindglam.agora.service;

import com.bindglam.agora.post.Post;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Optional<PostDTO> getPost(long postId) {
        return postRepository.findById(postId).map(Post::toDTO);
    }

    @Override
    public Page<PostDTO> getPostsByBoardId(String boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findByBoardId(boardId, pageable).map(Post::toDTO);
    }

    @Override
    public PostDTO savePost(PostDTO post) {
        return postRepository.save(post.toEntity()).toDTO();
    }
}
