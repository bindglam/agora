package com.bindglam.agora.post;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.registry.BuiltInRegistries;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Builder.Default
    private Long id = null;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String boardId;

    private LocalDateTime createdAt;

    public @NonNull Board getBoard() {
        return BuiltInRegistries.BOARDS.getOrThrow(boardId);
    }

    public @NonNull PostDTO toDTO() {
        return PostDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .build();
    }
}
