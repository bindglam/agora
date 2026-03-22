package com.bindglam.agora.service;

import com.bindglam.agora.post.Post;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRepository;
import com.bindglam.agora.post.PostRequests;
import com.bindglam.agora.post.censor.CensorResult;
import com.bindglam.agora.post.censor.CensorServer;
import com.bindglam.agora.post.censor.LocalCensorServer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final CensorServer censorServer = new LocalCensorServer();

    @Override
    public Optional<PostDTO> getPost(long postId) {
        return postRepository.findById(postId).map(Post::toDTO);
    }

    @Override
    public Page<PostDTO> getPostsByBoardId(String boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findByBoardIdOrderByCreatedAtDesc(boardId, pageable).map(Post::toDTO);
    }

    @Override
    public int getTotalPosts(String boardId) {
        return postRepository.countByBoardId(boardId);
    }

    @Override
    public PostRequests.Create.Response createPost(PostDTO post) {
        PostDTO newPost = null;
        CensorResult censorResult = censorServer.tryCensor(post).join();

        if(censorResult.isGood())
            newPost = postRepository.save(post.toEntity()).toDTO();

        return PostRequests.Create.Response.builder()
                .post(newPost)
                .censorResult(censorResult)
                .build();
    }
}
