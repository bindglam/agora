package com.bindglam.agora.controller;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/board-api")
@RequiredArgsConstructor
public final class BoardController {
    private final PostService postService;

    @GetMapping(value = "/posts/{boardId}")
    public ResponseEntity<List<PostDTO>> getPost(@PathVariable String boardId) {
        Optional<Board> optional = BuiltInRegistries.BOARDS.get(boardId);
        if(optional.isPresent()) {
            Board board = optional.get();
            return ResponseEntity.ok(postService.getPostsByBoardId(board.id()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
