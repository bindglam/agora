package com.bindglam.agora;

import com.bindglam.agora.post.Post;
import com.bindglam.agora.post.PostRepository;
import com.bindglam.agora.registry.BuiltInRegistries;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class AgoraBackendApplicationTests {
    @Autowired
    private PostRepository postRepository;

    @Test
    void testPost() {
        postRepository.save(Post.builder()
                .title("테스트")
                .content("테스트 게시물입니다")
                .boardId(BuiltInRegistries.BOARDS.getOrThrow("free").id())
                .createdAt(LocalDateTime.now())
                .build()
        );
    }
}
