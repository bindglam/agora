package com.bindglam.agora.post.controller;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostRequests;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.post.service.PostService;
import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post-api")
@RequiredArgsConstructor
public final class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<PostDTO> getPost(@RequestParam(name = "user") String username, @PathVariable long postId) {
        UserDTO user = userService.getUser(username).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.of(postService.getPost(user, postId));
    }

    @GetMapping(value = "/posts/{boardId}")
    public ResponseEntity<List<PostDTO>> getPosts(@RequestParam(name = "user") String username, @PathVariable String boardId, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="size", defaultValue="20") int size) {
        UserDTO user = userService.getUser(username).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();

        if(page < 0 || size < 0)
            return ResponseEntity.badRequest().build();

        Optional<Board> optional = BuiltInRegistries.BOARDS.get(boardId);
        if(optional.isPresent()) {
            Board board = optional.get();
            return ResponseEntity.ok(postService.getPostsByBoardId(user, board.id(), page, size).toList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/total-posts/{boardId}")
    public ResponseEntity<Integer> getTotalPosts(@RequestParam(name = "user") String username, @PathVariable String boardId) {
        UserDTO user = userService.getUser(username).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();

        Optional<Board> optional = BuiltInRegistries.BOARDS.get(boardId);
        if(optional.isPresent()) {
            Board board = optional.get();
            return ResponseEntity.ok(postService.getTotalPosts(user, board.id()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/post")
    public ResponseEntity<PostRequests.Create.Response> createPost(@RequestBody PostRequests.Create request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @PostMapping(value = "/post/comment")
    public ResponseEntity<PostRequests.Comment.Response> createComment(@RequestBody PostRequests.Comment request) {
        return ResponseEntity.ok(postService.createComment(request));
    }
}
