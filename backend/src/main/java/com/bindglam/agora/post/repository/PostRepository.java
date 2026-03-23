package com.bindglam.agora.post.repository;

import com.bindglam.agora.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoardIdOrderByCreatedAtDesc(String boardId, Pageable pageable);

    int countByBoardId(String boardId);
}
