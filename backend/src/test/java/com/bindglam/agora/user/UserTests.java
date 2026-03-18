package com.bindglam.agora.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testPost() {
        System.out.println("------회원가입 테스트...------");
        userRepository.save(User.builder()
                .nickname("테스트 계정")
                .password("test1234")
                .build());
        userRepository.save(User.builder()
                .nickname("테스트 계정2")
                .password("test12346")
                .build());
        System.out.println("------회원가입 테스트 완료------");

        System.out.println("------계정 로드 테스트...------");
        userRepository.findAll().stream().map(User::toDTO).forEach(user -> {
            System.out.println(user.toString());
        });
        System.out.println("------계정 로드 테스트 완료------");
    }
}
