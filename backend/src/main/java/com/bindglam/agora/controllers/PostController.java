package com.bindglam.agora.controllers;

import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post-api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping(value = "/post/{postId}")
    public PostDTO getPost(@PathVariable long postId) {
        return postService.getPost(postId);
    }
}
