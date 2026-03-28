package com.bindglam.agora.post;

import com.bindglam.agora.post.censor.Censorable;
import com.bindglam.agora.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class Comment implements Censorable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Builder.Default
    private Long id = null;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    private Long postId;

    @ManyToOne
    @JoinColumn(name = "author_username")
    private User author;

    @Override
    public @Nullable String title() {
        return null;
    }

    @Override
    public @NonNull String content() {
        return content;
    }

    public @NonNull CommentDTO toDTO() {
        return CommentDTO.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .postId(postId)
                .author(author.toDTO())
                .build();
    }
}
