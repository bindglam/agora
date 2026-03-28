package com.bindglam.agora.post.repository;

import com.bindglam.agora.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
