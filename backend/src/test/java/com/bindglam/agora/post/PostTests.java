package com.bindglam.agora.post;

import com.bindglam.agora.registry.BuiltInRegistries;
import com.bindglam.agora.user.User;
import com.bindglam.agora.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class PostTests {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testPost() {
        System.out.println("------테스트용 계정 생성------");
        User testUser = userRepository.save(User.builder()
                .nickname("테스트 계정1")
                .password("12345678")
                .build());
        System.out.println("------테스트용 계정 생성 완료------");

        System.out.println("------게시물 업로드 테스트...------");
        postRepository.save(Post.builder()
                .title("테스트")
                .content("테스트 게시물입니다")
                .boardId(BuiltInRegistries.BOARDS.getOrThrow("free").id())
                .createdAt(LocalDateTime.now())
                .author(testUser)
                .build()
        );
        postRepository.save(Post.builder()
                .title("테스트2")
                .content("아쎄이")
                .boardId(BuiltInRegistries.BOARDS.getOrThrow("free").id())
                .createdAt(LocalDateTime.now())
                .author(testUser)
                .build()
        );
        System.out.println("------게시물 업로드 테스트 완료------");

        System.out.println("------게시물 로드 테스트...------");
        postRepository.findAll().stream().map(Post::toDTO).forEach(post -> {
            System.out.println(post.toString());
        });
        System.out.println("------게시물 로드 테스트 완료------");
    }
}
