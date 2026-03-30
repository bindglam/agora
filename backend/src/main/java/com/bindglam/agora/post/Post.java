package com.bindglam.agora.post;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class Post implements Censorable {
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
    @JoinColumn(name = "author_key")
    private User author;

    private PostVisibility visibility;

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Override
    public @Nullable String title() {
        return title;
    }

    @Override
    public @NonNull String content() {
        return content;
    }

    public @NonNull Board getBoard() {
        return BuiltInRegistries.BOARDS.getOrThrow(boardId);
    }

    public @NonNull DefaultPostDTO toDTO() {
        return DefaultPostDTO.builder()
                .postId(id)
                .title(title)
                .content(content)
                .boardId(boardId)
                .createdAt(createdAt)
                .author(author.toDTO())
                .visibility(visibility)
                .comments(new ArrayList<>(comments.stream().map(Comment::toDTO).toList()))
                .build();
    }
}
