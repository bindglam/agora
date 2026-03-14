package com.bindglam.agora.post;

import com.bindglam.agora.registry.BuiltInRegistries;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class PostTests {
    @Autowired
    private PostRepository postRepository;

    @Test
    void testPost() {
        System.out.println("------게시물 업로드 테스트...------");
        postRepository.save(PostEntity.builder()
                .title("테스트")
                .content("테스트 게시물입니다")
                .boardId(BuiltInRegistries.BOARDS.getOrThrow("free").id())
                .createdAt(LocalDateTime.now())
                .build()
        );
        postRepository.save(PostEntity.builder()
                .title("테스트2")
                .content("아쎄이")
                .boardId(BuiltInRegistries.BOARDS.getOrThrow("free").id())
                .createdAt(LocalDateTime.now())
                .build()
        );
        System.out.println("------게시물 업로드 테스트 완료------");

        System.out.println("------게시물 로드 테스트...------");
        postRepository.findAll().stream().map(PostEntity::toDTO).forEach(post -> {
            System.out.println(post.toString());
        });
        System.out.println("------게시물 로드 테스트 완료------");
    }
}
