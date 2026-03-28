package com.bindglam.agora.post.service;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.*;
import com.bindglam.agora.post.censor.GeminiServer;
import com.bindglam.agora.post.repository.CommentRepository;
import com.bindglam.agora.post.repository.PostRepository;
import com.bindglam.agora.post.censor.CensorResult;
import com.bindglam.agora.post.censor.CensorServer;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.user.User;
import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.UserPermission;
import com.bindglam.agora.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class PostServiceImpl implements PostService {
    private final UserService userService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final CensorServer censorServer = new GeminiServer();

    @Override
    public Optional<PostDTO> getPost(@NonNull UserDTO user, long postId) {
        return postRepository.findById(postId).map(Post::toDTO).filter(post -> post.visibility() == PostVisibility.PUBLIC || post.visibility() == PostVisibility.NOT_LISTED || user.permission().check(UserPermission.ADMIN));
    }

    @Override
    public Streamable<PostDTO> getPostsByBoardId(@NonNull UserDTO user, @NonNull String boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findByBoardIdOrderByCreatedAtDesc(boardId, pageable).map(Post::toDTO).filter(post -> post.visibility() == PostVisibility.PUBLIC || user.permission().check(UserPermission.ADMIN));
    }

    @Override
    public int getTotalPosts(@NonNull UserDTO user, @NonNull String boardId) {
        int total = postRepository.countByBoardIdAndVisibility(boardId, PostVisibility.PUBLIC);
        if(user.permission() == UserPermission.ADMIN) {
            // TODO : optimization
            total += postRepository.countByBoardIdAndVisibility(boardId, PostVisibility.PRIVATE)
                    + postRepository.countByBoardIdAndVisibility(boardId, PostVisibility.NOT_LISTED);
        }

        return total;
    }

    @Override
    public PostRequests.Create.@NonNull Response createPost(PostRequests.@NonNull Create request) {
        Board board = BuiltInRegistries.BOARDS.get(request.boardId()).orElse(null);
        if(board == null)
            return PostRequests.Create.Response.builder().errorMessage("알 수 없는 게시판입니다.").build();

        UserDTO userDTO = userService.getUser(request.authorUsername()).orElse(null);
        if(userDTO == null)
            return PostRequests.Create.Response.builder().errorMessage("알 수 없는 유저입니다.").build();
        User user = userDTO.toEntity();

        if(!user.getPermission().check(board.properties().getPermissions().getPosting()))
            return PostRequests.Create.Response.builder().errorMessage("권한이 없습니다.").build();

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .boardId(request.boardId())
                .createdAt(LocalDateTime.now())
                .author(user)
                .visibility(PostVisibility.PUBLIC)
                .build();
        if(board.properties().isTryCensor()) {
            CensorResult censorResult = censorServer.tryCensor(post).join();

            if (censorResult.isGood())
                post = postRepository.save(post);

            return PostRequests.Create.Response.builder()
                    .post(post.toDTO())
                    .censorResult(censorResult)
                    .build();
        } else {
            post = postRepository.save(post);

            return PostRequests.Create.Response.builder()
                    .post(post.toDTO())
                    .censorResult(null)
                    .build();
        }
    }

    @Override
    public PostRequests.Comment.@NonNull Response createComment(PostRequests.@NonNull Comment request) {
        UserDTO userDTO = userService.getUser(request.authorUsername()).orElse(null);
        if(userDTO == null)
            return PostRequests.Comment.Response.builder().errorMessage("알 수 없는 유저입니다.").build();
        User user = userDTO.toEntity();

        Post post = getPost(userDTO, request.postId()).map(PostDTO::toEntity).orElse(null);
        if(post == null)
            return PostRequests.Comment.Response.builder().errorMessage("알 수 없는 게시글입니다.").build();

        Board board = post.getBoard();
        if(!userDTO.permission().check(board.properties().getPermissions().getComment()))
            return PostRequests.Comment.Response.builder().errorMessage("권한이 없습니다.").build();

        Comment comment = Comment.builder()
                .postId(post.getId())
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .author(user)
                .build();
        if(board.properties().isTryCensor()) {
            CensorResult censorResult = censorServer.tryCensor(comment).join();

            if (censorResult.isGood()) {
                comment = commentRepository.save(comment);
                post.getComments().add(comment);
            }

            return PostRequests.Comment.Response.builder()
                    .comment(comment.toDTO())
                    .censorResult(censorResult)
                    .build();
        } else {
            comment = commentRepository.save(comment);
            post.getComments().add(comment);

            return PostRequests.Comment.Response.builder()
                    .comment(comment.toDTO())
                    .censorResult(null)
                    .build();
        }
    }
}
