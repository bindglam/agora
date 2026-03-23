package com.bindglam.agora.post;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.user.User;
import com.bindglam.agora.utils.Identified;
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
public final class Post implements Identified {
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

    @ManyToOne
    @JoinColumn(name = "author_username")
    private User author;

    private PostVisibility visibility;

    @Override
    public @NonNull String id() {
        return String.valueOf(id);
    }

    public @NonNull Board getBoard() {
        return BuiltInRegistries.BOARDS.getOrThrow(boardId);
    }

    public @NonNull PostDTO toDTO() {
        return PostDTO.builder()
                .postId(id)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author.toDTO())
                .visibility(visibility)
                .build();
    }
}
