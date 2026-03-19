package com.bindglam.agora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AgoraBackendApplication {
    static void main(String[] args) {
        SpringApplication.run(AgoraBackendApplication.class, args);
    }
}
