package com.bindglam.agora.controller;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.registry.BuiltInRegistries;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/board-api")
@RequiredArgsConstructor
public final class BoardController {
    @GetMapping(value = "/boards")
    public ResponseEntity<List<Board>> getBoards() {
        return ResponseEntity.ok(BuiltInRegistries.BOARDS.entries().stream().map(Map.Entry::getValue).toList());
    }

    @GetMapping(value = "/board/{boardId}")
    public ResponseEntity<Board> getBoard(@PathVariable String boardId) {
        return ResponseEntity.of(BuiltInRegistries.BOARDS.get(boardId));
    }
}
