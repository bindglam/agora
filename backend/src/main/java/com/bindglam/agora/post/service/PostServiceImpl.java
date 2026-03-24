package com.bindglam.agora.post.service;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.Post;
import com.bindglam.agora.post.PostDTO;
import com.bindglam.agora.post.PostVisibility;
import com.bindglam.agora.post.repository.PostRepository;
import com.bindglam.agora.post.PostRequests;
import com.bindglam.agora.post.censor.CensorResult;
import com.bindglam.agora.post.censor.CensorServer;
import com.bindglam.agora.post.censor.LocalCensorServer;
import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.user.UserDTO;
import com.bindglam.agora.user.UserPermission;
import com.bindglam.agora.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class PostServiceImpl implements PostService {
    private final UserService userService;

    private final PostRepository postRepository;

    private final CensorServer censorServer = new LocalCensorServer();

    @Override
    public Optional<PostDTO> getPost(@NonNull UserDTO user, long postId) {
        return postRepository.findById(postId).map(Post::toDTO).filter(post -> post.visibility() == PostVisibility.PUBLIC || post.visibility() == PostVisibility.NOT_LISTED || user.permission().check(UserPermission.ADMIN));
    }

    @Override
    public Streamable<PostDTO> getPostsByBoardId(@NonNull UserDTO user, String boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findByBoardIdOrderByCreatedAtDesc(boardId, pageable).map(Post::toDTO).filter(post -> post.visibility() == PostVisibility.PUBLIC || user.permission().check(UserPermission.ADMIN));
    }

    @Override
    public int getTotalPosts(@NonNull UserDTO user, String boardId) {
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

        UserDTO user = userService.getUser(request.authorUsername()).orElse(null);
        if(user == null)
            return PostRequests.Create.Response.builder().errorMessage("알 수 없는 유저입니다.").build();

        if(!user.permission().check(board.properties().getPermissions().getPosting()))
            return PostRequests.Create.Response.builder().errorMessage("권한이 없습니다.").build();

        PostDTO postDTO = PostDTO.builder()
                .title(request.title())
                .content(request.content())
                .boardId(request.boardId())
                .createdAt(LocalDateTime.now())
                .author(user)
                .visibility(PostVisibility.PUBLIC)
                .build();
        CensorResult censorResult = censorServer.tryCensor(postDTO).join();

        if(censorResult.isGood())
            postDTO = postRepository.save(postDTO.toEntity()).toDTO();

        return PostRequests.Create.Response.builder()
                .post(postDTO)
                .censorResult(censorResult)
                .build();
    }
}
