package com.bindglam.agora.post.controller;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRequests;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post-api")
@RequiredArgsConstructor
public final class PostController {
    private final PostService postService;

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable long postId) {
        return ResponseEntity.of(postService.getPost(postId));
    }

    @GetMapping(value = "/posts/{boardId}")
    public ResponseEntity<List<PostDTO>> getPosts(@PathVariable String boardId, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="size", defaultValue="20") int size) {
        if(page < 0 || size < 0)
            return ResponseEntity.badRequest().build();

        Optional<Board> optional = BuiltInRegistries.BOARDS.get(boardId);
        if(optional.isPresent()) {
            Board board = optional.get();
            return ResponseEntity.ok(postService.getPostsByBoardId(board.id(), page, size).toList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/total-posts/{boardId}")
    public ResponseEntity<Integer> getTotalPosts(@PathVariable String boardId) {
        Optional<Board> optional = BuiltInRegistries.BOARDS.get(boardId);
        if(optional.isPresent()) {
            Board board = optional.get();
            return ResponseEntity.ok(postService.getTotalPosts(board.id()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/post")
    public ResponseEntity<PostRequests.Create.Response> createPost(@RequestBody PostRequests.Create request) {
        // TODO : permission check
        PostDTO postDTO = PostDTO.builder()
                .title(request.title())
                .content(request.content())
                .boardId(request.boardId())
                .createdAt(LocalDateTime.now())
                .author(request.author())
                .isPublic(false) // TODO : true
                .build();

        return ResponseEntity.ok(postService.createPost(postDTO));
    }
}
