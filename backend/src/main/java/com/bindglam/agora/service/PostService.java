package com.bindglam.agora.service;

import com.bindglam.agora.post.PostDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<PostDTO> getPost(long postId);

    Page<PostDTO> getPostsByBoardId(String boardId, int page, int size);

    PostDTO savePost(PostDTO post);
}
