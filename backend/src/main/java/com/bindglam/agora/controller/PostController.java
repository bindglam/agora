package com.bindglam.agora.controller;

import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post-api")
@RequiredArgsConstructor
public final class PostController {
    private final PostService postService;

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable long postId) {
        return ResponseEntity.of(postService.getPost(postId));
    }
}
